package com.tyron.layouteditor.adapters.gson;

import android.content.Context;

import com.tyron.layouteditor.editor.FunctionManager;
import com.tyron.layouteditor.editor.Editor;
import com.tyron.layouteditor.editor.EditorConstants;
import com.tyron.layouteditor.editor.ViewTypeParser;
import com.tyron.layouteditor.processor.AttributeProcessor;
import com.tyron.layouteditor.values.Array;
import com.tyron.layouteditor.values.Binding;
import com.tyron.layouteditor.values.Layout;
import com.tyron.layouteditor.values.Null;
import com.tyron.layouteditor.values.ObjectValue;
import com.tyron.layouteditor.values.Primitive;
import com.tyron.layouteditor.values.Value;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * EditorTypeAdapterFactory
 *
 * @author aditya.sharat
 */
public class EditorTypeAdapterFactory implements TypeAdapterFactory {

  public static final EditorInstanceHolder EDITOR_INSTANCE_HOLDER = new EditorInstanceHolder();

  Context context;

  /**
   *
   */
  public final TypeAdapter<Value> VALUE_TYPE_ADAPTER = new TypeAdapter<Value>() {
    @Override
    public void write(JsonWriter out, Value value) throws IOException {
      throw new UnsupportedOperationException("Use EditorTypeAdapterFactory.COMPILED_VALUE_TYPE_ADAPTER instead");
    }

    @Override
    public Value read(JsonReader in) throws IOException {
      switch (in.peek()) {
        case STRING:
          return compileString(getContext(), in.nextString());
        case NUMBER:
          String number = in.nextString();
          return new Primitive(new LazilyParsedNumber(number));
        case BOOLEAN:
          return new Primitive(in.nextBoolean());
        case NULL:
          in.nextNull();
          return Null.INSTANCE;
        case BEGIN_ARRAY:
          Array array = new Array();
          in.beginArray();
          while (in.hasNext()) {
            array.add(read(in));
          }
          in.endArray();
          return array;
        case BEGIN_OBJECT:
          ObjectValue object = new ObjectValue();
          in.beginObject();
          if (in.hasNext()) {
            String name = in.nextName();
            if (EditorConstants.TYPE.equals(name) && JsonToken.STRING.equals(in.peek())) {
              String type = in.nextString();
              if (EDITOR_INSTANCE_HOLDER.isLayout(type)) {
                Layout layout = LAYOUT_TYPE_ADAPTER.read(type, EDITOR_INSTANCE_HOLDER.getEditor(), in);
                in.endObject();
                return layout;
              } else {
                object.add(name, compileString(getContext(), type));
              }
            } else {
              object.add(name, read(in));
            }
          }
          while (in.hasNext()) {
            object.add(in.nextName(), read(in));
          }
          in.endObject();
          return object;
        case END_DOCUMENT:
        case NAME:
        case END_OBJECT:
        case END_ARRAY:
        default:
          throw new IllegalArgumentException();
      }
    }
  }.nullSafe();

  /**
   *
   */
  public final TypeAdapter<Primitive> PRIMITIVE_TYPE_ADAPTER = new TypeAdapter<Primitive>() {

    @Override
    public void write(JsonWriter out, Primitive value) throws IOException {
      VALUE_TYPE_ADAPTER.write(out, value);
    }

    @Override
    public Primitive read(JsonReader in) throws IOException {
      Value value = VALUE_TYPE_ADAPTER.read(in);
      return value != null && value.isPrimitive() ? value.getAsPrimitive() : null;
    }
  }.nullSafe();

  /**
   *
   */
  public final TypeAdapter<ObjectValue> OBJECT_TYPE_ADAPTER = new TypeAdapter<ObjectValue>() {
    @Override
    public void write(JsonWriter out, ObjectValue value) throws IOException {
      VALUE_TYPE_ADAPTER.write(out, value);
    }

    @Override
    public ObjectValue read(JsonReader in) throws IOException {
      Value value = VALUE_TYPE_ADAPTER.read(in);
      return value != null && value.isObject() ? value.getAsObject() : null;
    }
  }.nullSafe();

  /**
   *
   */
  public final TypeAdapter<Array> ARRAY_TYPE_ADAPTER = new TypeAdapter<Array>() {
    @Override
    public void write(JsonWriter out, Array value) throws IOException {
      VALUE_TYPE_ADAPTER.write(out, value);
    }

    @Override
    public Array read(JsonReader in) throws IOException {
      Value value = VALUE_TYPE_ADAPTER.read(in);
      return value != null && value.isArray() ? value.getAsArray() : null;
    }
  }.nullSafe();

  /**
   *
   */
  public final TypeAdapter<Null> NULL_TYPE_ADAPTER = new TypeAdapter<Null>() {

    @Override
    public void write(JsonWriter out, Null value) throws IOException {
      VALUE_TYPE_ADAPTER.write(out, value);
    }

    @Override
    public Null read(JsonReader in) throws IOException {
      Value value = VALUE_TYPE_ADAPTER.read(in);
      return value != null && value.isNull() ? value.getAsNull() : null;
    }
  }.nullSafe();

  /**
   *
   */
  public final LayoutTypeAdapter LAYOUT_TYPE_ADAPTER = new LayoutTypeAdapter();

  /**
   *
   */
  public final TypeAdapter<Value> COMPILED_VALUE_TYPE_ADAPTER = new TypeAdapter<Value>() {

    public static final String TYPE = "type";
    public static final String VALUE = "value";

    @Override
    public void write(JsonWriter out, Value value) throws IOException {
      if (value == null || value.isNull()) {
        out.nullValue();
      } else if (value.isPrimitive()) {
        Primitive primitive = value.getAsPrimitive();
        if (primitive.isNumber()) {
          out.value(primitive.getAsNumber());
        } else if (primitive.isBoolean()) {
          out.value(primitive.getAsBoolean());
        } else {
          out.value(primitive.getAsString());
        }
      } else if (value.isObject()) {
        out.beginObject();
        for (Map.Entry<String, Value> e : value.getAsObject().entrySet()) {
          out.name(e.getKey());
          write(out, e.getValue());
        }
        out.endObject();
      } else if (value.isArray()) {
        out.beginArray();
        Iterator<Value> iterator = value.getAsArray().iterator();
        while (iterator.hasNext()) {
          write(out, iterator.next());
        }
        out.endArray();
      } else {
        CustomValueTypeAdapter adapter = getCustomValueTypeAdapter(value.getClass());

        out.beginObject();

        out.name(TYPE);
        out.value(adapter.type);

        out.name(VALUE);

        //noinspection unchecked
        adapter.write(out, value);

        out.endObject();
      }
    }

    @Override
    public Value read(JsonReader in) throws IOException {
      switch (in.peek()) {
        case STRING:
          return compileString(getContext(), in.nextString());
        case NUMBER:
          String number = in.nextString();
          return new Primitive(new LazilyParsedNumber(number));
        case BOOLEAN:
          return new Primitive(in.nextBoolean());
        case NULL:
          in.nextNull();
          return Null.INSTANCE;
        case BEGIN_ARRAY:
          Array array = new Array();
          in.beginArray();
          while (in.hasNext()) {
            array.add(read(in));
          }
          in.endArray();
          return array;
        case BEGIN_OBJECT:
          ObjectValue object = new ObjectValue();
          in.beginObject();
          if (in.hasNext()) {
            String name = in.nextName();
            if (TYPE.equals(name) && JsonToken.NUMBER.equals(in.peek())) {
              int type = Integer.parseInt(in.nextString());
              CustomValueTypeAdapter<? extends Value> adapter = getCustomValueTypeAdapter(type);
              in.nextName();
              Value value = adapter.read(in);
              in.endObject();
              return value;
            } else {
              object.add(name, read(in));
            }
          }
          while (in.hasNext()) {
            object.add(in.nextName(), read(in));
          }
          in.endObject();
          return object;
        case END_DOCUMENT:
        case NAME:
        case END_OBJECT:
        case END_ARRAY:
        default:
          throw new IllegalArgumentException();
      }
    }

  };


  /**
   *
   */
  private CustomValueTypeAdapterMap map = new CustomValueTypeAdapterMap();

  public static final String ARRAYS_DELIMITER = "|";
  public static final String ARRAY_DELIMITER = ",";

  public static String writeArrayOfInts(int[] array) {
    StringBuilder builder = new StringBuilder();
    for (int index = 0; index < array.length; index++) {
      builder.append(array[index]);
      if (index < array.length - 1) {
        builder.append(ARRAY_DELIMITER);
      }
    }
    return builder.toString();
  }

  public static String writeArrayOfIntArrays(int[][] arrays) {
    StringBuilder builder = new StringBuilder();
    for (int index = 0; index < arrays.length; index++) {
      builder.append(writeArrayOfInts(arrays[index]));
      if (index < arrays.length - 1) {
        builder.append(ARRAYS_DELIMITER);
      }
    }
    return builder.toString();
  }

  public static int[] readArrayOfInts(String string) {
    int[] array = new int[0];
    StringTokenizer tokenizer = new StringTokenizer(string, ARRAY_DELIMITER);
    while (tokenizer.hasMoreTokens()) {
      array = Arrays.copyOf(array, array.length + 1);
      array[array.length - 1] = Integer.parseInt(tokenizer.nextToken());
    }
    return array;
  }

  public static int[][] readArrayOfIntArrays(String string) {
    int[][] arrays = new int[0][];
    StringTokenizer tokenizer = new StringTokenizer(string, ARRAYS_DELIMITER);
    while (tokenizer.hasMoreTokens()) {
      arrays = Arrays.copyOf(arrays, arrays.length + 1);
      arrays[arrays.length - 1] = readArrayOfInts(tokenizer.nextToken());
    }
    return arrays;
  }

  /**
   * @param context
   */
  public EditorTypeAdapterFactory(Context context) {
    this.context = context;
    DefaultModule.create().register(this);
  }

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    Class clazz = type.getRawType();

    if (clazz == Primitive.class) {
      //noinspection unchecked
      return (TypeAdapter<T>) PRIMITIVE_TYPE_ADAPTER;
    } else if (clazz == ObjectValue.class) {
      //noinspection unchecked
      return (TypeAdapter<T>) OBJECT_TYPE_ADAPTER;
    } else if (clazz == Array.class) {
      //noinspection unchecked
      return (TypeAdapter<T>) ARRAY_TYPE_ADAPTER;
    } else if (clazz == Null.class) {
      //noinspection unchecked
      return (TypeAdapter<T>) NULL_TYPE_ADAPTER;
    } else if (clazz == Layout.class) {
      //noinspection unchecked
      return (TypeAdapter<T>) LAYOUT_TYPE_ADAPTER;
    } else if (clazz == Value.class) {
      //noinspection unchecked
      return (TypeAdapter<T>) VALUE_TYPE_ADAPTER;
    }

    return null;
  }

  public void register(Class<? extends Value> clazz, CustomValueTypeAdapterCreator<? extends Value> creator) {
    map.register(clazz, creator);
  }

  public CustomValueTypeAdapter<? extends Value> getCustomValueTypeAdapter(Class<? extends Value> clazz) {
    return map.get(clazz);
  }

  public CustomValueTypeAdapter<? extends Value> getCustomValueTypeAdapter(int type) {
    return map.get(type);
  }

  public Context getContext() {
    return context;
  }

  static Value compileString(Context context, String string) {
    if (Binding.isBindingValue(string)) {
      return Binding.valueOf(string, context, EDITOR_INSTANCE_HOLDER.getEditor().functions);
    } else {
      return new Primitive(string);
    }
  }

  public interface Module {

    /**
     * @param factory
     */
    void register(EditorTypeAdapterFactory factory);

  }

  public static class EditorInstanceHolder {

    private Editor editor;

    EditorInstanceHolder() {
    }

    public Editor getEditor() {
      return editor;
    }

    public void setEditor(Editor editor) {
      this.editor = editor;
    }

    public boolean isLayout(String type) {
      return null != editor && editor.has(type);
    }
  }

  public class LayoutTypeAdapter extends TypeAdapter<Layout> {

    @Override
    public void write(JsonWriter out, Layout value) throws IOException {
      VALUE_TYPE_ADAPTER.write(out, value);
    }

    @Override
    public Layout read(JsonReader in) throws IOException {
      Value value = VALUE_TYPE_ADAPTER.read(in);
      return value != null && value.isLayout() ? value.getAsLayout() : null;
    }

    public Layout read(String type, Editor editor, JsonReader in) throws IOException {
      List<Layout.Attribute> attributes = new ArrayList<>();
      Map<String, Value> data = null;
      ObjectValue extras = new ObjectValue();
      String name;
      while (in.hasNext()) {
        name = in.nextName();
        if (EditorConstants.DATA.equals(name)) {
          data = readData(in);
        } else {
          ViewTypeParser.AttributeSet.Attribute attribute = editor.getAttributeId(name, type);
          if (null != attribute) {
            FunctionManager manager = EDITOR_INSTANCE_HOLDER.getEditor().functions;
            Value value = attribute.processor.precompile(VALUE_TYPE_ADAPTER.read(in), getContext(), manager);
            attributes.add(new Layout.Attribute(attribute.id, value));
          } else {
            extras.add(name, VALUE_TYPE_ADAPTER.read(in));
          }
        }
      }
      return new Layout(type, attributes.size() > 0 ? attributes : null, data, extras.entrySet().size() > 0 ? extras : null);
    }

    public Map<String, Value> readData(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek == JsonToken.NULL) {
        in.nextNull();
        return new HashMap<>();
      }

      if (peek != JsonToken.BEGIN_OBJECT) {
        throw new JsonSyntaxException("data must be a Map<String, String>.");
      }

      Map<String, Value> data = new LinkedHashMap<>();

      in.beginObject();
      while (in.hasNext()) {
        JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
        String key = in.nextString();
        Value value = VALUE_TYPE_ADAPTER.read(in);
        Value compiled = AttributeProcessor.staticPreCompile(value, context, EDITOR_INSTANCE_HOLDER.getEditor().functions);
        if (compiled != null) {
          value = compiled;
        }
        Value replaced = data.put(key, value);
        if (replaced != null) {
          throw new JsonSyntaxException("duplicate key: " + key);
        }
      }
      in.endObject();

      return data;
    }
  }

  private class CustomValueTypeAdapterMap {

    private final Map<Class<? extends Value>, CustomValueTypeAdapter<? extends Value>> types = new HashMap<>();

    private CustomValueTypeAdapter<? extends Value>[] adapters = new CustomValueTypeAdapter[0];

    CustomValueTypeAdapterMap() {
    }

    public CustomValueTypeAdapter<? extends Value> register(Class<? extends Value> clazz, CustomValueTypeAdapterCreator creator) {
      CustomValueTypeAdapter<? extends Value> adapter = types.get(clazz);
      if (null != adapter) {
        return adapter;
      }
      //noinspection unchecked
      adapter = creator.create(adapters.length, EditorTypeAdapterFactory.this);
      adapters = Arrays.copyOf(adapters, adapters.length + 1);
      adapters[adapters.length - 1] = adapter;
      return types.put(clazz, adapter);
    }

    public CustomValueTypeAdapter<? extends Value> get(Class<? extends Value> clazz) {
      CustomValueTypeAdapter i = types.get(clazz);
      if (null == i) {
        throw new IllegalArgumentException(clazz.getName() + " is not a known value type! Remember to register the class first");
      }
      return types.get(clazz);
    }

    public CustomValueTypeAdapter<? extends Value> get(int i) {
      if (i < adapters.length) {
        return adapters[i];
      }
      throw new IllegalArgumentException(i + " is not a known value type! Did you conjure up this int?");
    }
  }
}