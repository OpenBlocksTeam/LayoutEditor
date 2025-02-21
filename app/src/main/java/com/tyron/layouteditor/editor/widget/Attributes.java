package com.tyron.layouteditor.editor.widget;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;

import com.tyron.layouteditor.editor.EditorContext;
import com.tyron.layouteditor.editor.IdGenerator;
import com.tyron.layouteditor.editor.widget.viewgroup.RelativeLayoutItem;
import com.tyron.layouteditor.models.Attribute;
import com.tyron.layouteditor.values.Dimension;
import com.tyron.layouteditor.values.DrawableValue;
import com.tyron.layouteditor.values.Null;
import com.tyron.layouteditor.values.Primitive;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that holds all the view attributes
 */
public class Attributes {

    public static HashMap<String,Integer> types = new HashMap<>();

    public static final int TYPE_NUMBER = 1;
    public static final int TYPE_BOOLEAN = 2;
    public static final int TYPE_STRING = 3;
	public static final int TYPE_DRAWABLE_STRING = 6;
    public static final int TYPE_LAYOUT_STRING = 4;
	public static final int TYPE_DIMENSION = 5;
	public static final int TYPE_COLOR = 7;

    static{
        types.put(View.Weight, TYPE_NUMBER);
        types.put(View.Width, TYPE_DIMENSION);
        types.put(View.Height, TYPE_DIMENSION);
        types.put(View.Elevation, TYPE_DIMENSION);

        types.put(LinearLayout.Orientation, TYPE_NUMBER);
        types.put(ProgressBar.Max, TYPE_NUMBER);

        types.put(View.AlignParentBottom, TYPE_BOOLEAN);
        types.put(View.AlignParentTop, TYPE_BOOLEAN);
        types.put(View.AlignParentStart, TYPE_BOOLEAN);
        types.put(View.AlignParentEnd, TYPE_BOOLEAN);
        types.put(View.AlignParentLeft, TYPE_BOOLEAN);
        types.put(View.AlignParentRight, TYPE_BOOLEAN);
        types.put(View.CenterHorizontal, TYPE_BOOLEAN);
        types.put(View.CenterVertical, TYPE_BOOLEAN);
        types.put(View.CenterInParent, TYPE_BOOLEAN);
        types.put(View.Enabled, TYPE_BOOLEAN);
        types.put(ProgressBar.Indeterminate, TYPE_BOOLEAN);

        types.put(View.ToLeftOf, TYPE_LAYOUT_STRING);
        types.put(View.ToRightOf, TYPE_LAYOUT_STRING);
        types.put(View.ToStartOf, TYPE_LAYOUT_STRING);
        types.put(View.ToEndOf, TYPE_LAYOUT_STRING);
        types.put(View.Above, TYPE_LAYOUT_STRING);
        types.put(View.Below, TYPE_LAYOUT_STRING);

        types.put(TextView.Text, TYPE_STRING);
		types.put(View.Background, TYPE_DRAWABLE_STRING | TYPE_COLOR);
		types.put(View.Id, TYPE_STRING);

		types.put(TextView.TextColor, TYPE_COLOR);
    }
    public static class View {

        public static final String Above = "android:layout_above";
        public static final String Activated = "android:activated";
        public static final String AlignBaseline = "android:layout_alignBaseline";
        public static final String AlignBottom = "android:layout_alignBottom";
        public static final String AlignEnd = "android:layout_alignEnd";
        public static final String AlignLeft = "android:layout_alignLeft";
        public static final String AlignParentBottom = "android:layout_alignParentBottom";
        public static final String AlignParentEnd = "android:layout_alignParentEnd";
        public static final String AlignParentLeft = "android:layout_alignParentLeft";
        public static final String AlignParentRight = "android:layout_alignParentRight";
        public static final String AlignParentStart = "android:layout_alignParentStart";
        public static final String AlignParentTop = "android:layout_alignParentTop";
        public static final String AlignRight = "android:layout_alignRight";
        public static final String AlignStart = "android:layout_alignStart";
        public static final String AlignTop = "android:layout_alignTop";
        public static final String Alpha = "android:alpha";
        public static final String Animation = "android:animation";
        public static final String Background = "android:background";
        public static final String BackgroundTint = "android:backgroundTint";
        public static final String BackgroundTintMode = "android:backgroundTintMode";
        public static final String Below = "android:layout_below";
        public static final String CenterHorizontal = "android:layout_centerHorizontal";
        public static final String CenterInParent = "android:layout_centerInParent";
        public static final String CenterVertical = "android:layout_centerVertical";
        public static final String Clickable = "android:clickable";
        public static final String ContentDescription = "android:contentDescription";
        public static final String ContextClickable = "android:contextClickable";
        public static final String DrawingCacheQuality = "android:drawingCacheQuality";
        public static final String Elevation = "android:elevation";
        public static final String Enabled = "android:enabled";
        public static final String FadeScrollbars = "android:fadeScrollbars";
        public static final String FadingEdgeLength = "android:fadingEdgeLength";
        public static final String FilterTouchesWhenObscured = "android:filterTouchesWhenObscured";
        public static final String FitsSystemWindows = "android:fitsSystemWindows";
        public static final String Focusable = "android:focusable";
        public static final String FocusableInTouchMode = "android:focusableInTouchMode";
        public static final String ForceHasOverlappingRendering = "android:forceHasOverlappingRendering";
        public static final String Foreground = "android:foreground";
        public static final String ForegroundGravity = "android:foregroundGravity";
        public static final String ForegroundTint = "android:foregroundTint";
        public static final String ForegroundTintMode = "android:foregroundTintMode";
        public static final String Gravity = "android:gravity";
        public static final String HapticFeedbackEnabled = "android:hapticFeedbackEnabled";
        public static final String Height = "android:layout_height";
        public static final String Id = "android:id";
        public static final String IsScrollContainer = "android:isScrollContainer";
        public static final String KeepScreenOn = "android:keepScreenOn";
        public static final String LayerType = "android:layerType";
        public static final String LayoutDirection = "android:layoutDirection";
        public static final String LayoutGravity = "android:layout_gravity";
        public static final String LongClickable = "android:longClickable";
        public static final String Margin = "android:layout_margin";
        public static final String MarginBottom = "android:layout_marginBottom";
        public static final String MarginLeft = "android:layout_marginLeft";
        public static final String MarginRight = "android:layout_marginRight";
        public static final String MarginTop = "android:layout_marginTop";
        public static final String MinHeight = "android:minHeight";
        public static final String MinWidth = "android:minWidth";
        public static final String NextFocusForward = "android:nextFocusForward";
        public static final String NextFocusLeft = "android:nextFocusLeft";
        public static final String NextFocusRight = "android:nextFocusRight";
        public static final String NextFocusUp = "android:nextFocusUp";
        public static final String OnClick = "android:onClick";
        public static final String OnLongClick = "android:onLongClick";
        public static final String OnTouch = "android:onTouch";
        public static final String Padding = "android:padding";
        public static final String PaddingBottom = "android:paddingBottom";
        public static final String PaddingEnd = "android:paddingEnd";
        public static final String PaddingLeft = "android:paddingLeft";
        public static final String PaddingRight = "android:paddingRight";
        public static final String PaddingStart = "android:paddingStart";
        public static final String PaddingTop = "android:paddingTop";
        public static final String RequiresFadingEdge = "android:requiresFadingEdge";
        public static final String Rotation = "android:rotation";
        public static final String RotationX = "android:rotationX";
        public static final String RotationY = "android:rotationY";
        public static final String SaveEnabled = "android:saveEnabled";
        public static final String ScaleX = "android:scaleX";
        public static final String ScaleY = "android:scaleY";
        public static final String ScrollIndicators = "android:scrollIndicators";
        public static final String ScrollbarDefaultDelayBeforeFade = "android:scrollbarDefaultDelayBeforeFade";
        public static final String ScrollbarFadeDuration = "android:scrollbarFadeDuration";
        public static final String ScrollbarSize = "android:scrollbarSize";
        public static final String ScrollbarStyle = "android:scrollbarStyle";
        public static final String Selected = "android:selected";
        public static final String SoundEffectsEnabled = "android:soundEffectsEnabled";
        public static final String Style = "android:style";
        public static final String Tag = "android:tag";
        public static final String TextAlignment = "android:textAlignment";
        public static final String TextDirection = "android:textDirection";
        public static final String ToEndOf = "android:layout_toEndOf";
        public static final String ToLeftOf = "android:layout_toLeftOf";
        public static final String ToRightOf = "android:layout_toRightOf";
        public static final String ToStartOf = "android:layout_toStartOf";
        public static final String TransformPivotX = "android:transformPivotX";
        public static final String TransformPivotY = "android:transformPivotY";
        public static final String TransitionName = "android:transitionName";
        public static final String TranslationX = "android:translationX";
        public static final String TranslationY = "android:translationY";
        public static final String TranslationZ = "android:translationZ";
        public static final String Visibility = "android:visibility";
        public static final String Weight = "android:layout_weight";
        public static final String Width = "android:layout_width";
    }

    public static class WebView {
        public static final String Url = "url";
        public static final String HTML = "html";
    }

    public static class RatingBar {
        public static final String NumStars = "numStars";
        public static final String Rating = "rating";
        public static final String IsIndicator = "isIndicator";
        public static final String StepSize = "stepSize";
        public static final String ProgressDrawable = "progressDrawable";
        public static final String MinHeight = "minHeight";
    }

    public static class TextView {
        public static final String Gravity = "android:gravity";
        public static final String Text = "android:text";
        public static final String HTML = "android:html";
        public static final String TextSize = "android:textSize";
        public static final String TextColor = "android:textColor";
        public static final String TextColorHint = "android:textColorHint";
        public static final String TextColorLink = "android:textColorLink";
        public static final String TextColorHighLight = "android:textColorHighlight";
        public static final String DrawableLeft = "android:drawableLeft";
        public static final String DrawableRight = "android:drawableRight";
        public static final String DrawableTop = "android:drawableTop";
        public static final String DrawableBottom = "android:drawableBottom";
        public static final String DrawablePadding = "android:drawablePadding";
        public static final String MaxLines = "android:maxLines";
        public static final String Ellipsize = "android:ellipsize";
        public static final String PaintFlags = "android:paintFlags";
        public static final String Prefix = "android:prefix";
        public static final String Suffix = "android:suffix";
        public static final String TextStyle = "android:textStyle";
        public static final String SingleLine = "android:singleLine";
        public static final String TextAllCaps = "android:textAllCaps";
        public static final String Hint = "android:hint";
    }

    public static class CheckBox {
        public static final String Checked = "android:checked";
        public static final String Button = "android:button";
    }

    public static class FrameLayout {
        public static final String HeightRatio = "android:heightRatio";
        public static final String WidthRatio = "android:widthRatio";
    }

    public static class ImageView {
        public static final String Src = "android:src";
        public static final String ScaleType = "android:scaleType";
        public static final String AdjustViewBounds = "android:adjustViewBounds";
    }

    public static class ViewGroup {
        public static final String Children = "android:children";
        public static final String ClipChildren = "android:clipChildren";
        public static final String ClipToPadding = "android:clipToPadding";
        public static final String LayoutMode = "android:layoutMode";
        public static final String SplitMotionEvents = "android:splitMotionEvents";
    }

    public static class LinearLayout {
        public static final String Orientation = "android:orientation";
        public static final String Divider = "android:divider";
        public static final String DividerPadding = "android:dividerPadding";
        public static final String ShowDividers = "android:showDividers";
        public static final String WeightSum = "android:weightSum";
    }

    public static class ScrollView {
        public static final String Scrollbars = "android:scrollbars";
    }

    public static class HorizontalScrollView {
        public static final String FillViewPort = "android:fillViewPort";
    }

    public static class ProgressBar {
        public static final String Progress = "android:progress";
        public static final String Max = "android:max";
        public static final String ProgressTint = "android:progressTint";
        public static final String IndeterminateTint = "android:indeterminateTint";
        public static final String Indeterminate = "android:indeterminate";
        public static final String SecondaryProgressTint = "android:secondaryProgressTint";
    }

    /**
     * @param string the view attribute
     * @return returns the type of the attribute
     */
    public static int getType(String string){
        Integer type = types.get(string);
		return type == null ? 0 : type;
    }
	
	public static ArrayList<Attribute> getViewAttributes(BaseWidget widget){

        EditorContext context = widget.getViewManager().getContext();
		ArrayList<Attribute> attrs = new ArrayList<>();
		android.view.ViewGroup.LayoutParams params = widget.getAsView().getLayoutParams();

		attrs.add(new Attribute(View.Width, Dimension.valueOf(params.width)));
		attrs.add(new Attribute(View.Height, Dimension.valueOf(params.height)));

		//get the background of a view if it's a Color, then we set it as a String
        Drawable drawable = widget.getAsView().getBackground();

        if(drawable == null){
            attrs.add(new Attribute(View.Background, new Primitive("#00FFFFFF")));
        }else if(drawable instanceof ColorDrawable){
            int color = ((ColorDrawable) drawable).getColor();
            String colorHex = String.format("#%06X", (0xFFFFFF & color));
            attrs.add(new Attribute(View.Background, new Primitive(colorHex)));
        }

        //TODO: HANDLE OBJECT VALUE FOR @drawable

        attrs.add(new Attribute(View.Id, new Primitive(context.getInflater().getIdGenerator().getString(widget.getAsView().getId()))));
		
		return attrs;
	}

    /**
     * @return returns the attributes of a child view with a parent of RelativeLayout
     */
    public static ArrayList<Attribute> getRelativeLayoutChildAttributes(BaseWidget widget){

        RelativeLayoutItem.LayoutParams params = (RelativeLayoutItem.LayoutParams) widget.getAsView().getLayoutParams();
        IdGenerator idGenerator = widget.getViewManager().getContext().getInflater().getIdGenerator();
        ArrayList<Attribute> arrayList = new ArrayList<>();


        int[] rules = params.getRules();
        //the index is the name of the rule and the actual value
        //is the value of the rule
        for(int i = 0; i < rules.length; i++){
            int rule = rules[i];
            
            switch(i){
                case RelativeLayout.LEFT_OF:
                    arrayList.add(new Attribute(Attributes.View.ToLeftOf, rule == 0 ? Null.INSTANCE : new Primitive(idGenerator.getString(rule))));
                    continue;
                case RelativeLayout.RIGHT_OF:
                    arrayList.add(new Attribute(Attributes.View.ToRightOf, rule == 0 ? Null.INSTANCE : new Primitive(idGenerator.getString(rule))));
                    continue;
                case RelativeLayout.ABOVE:
                    arrayList.add(new Attribute(Attributes.View.Above, rule == 0 ? Null.INSTANCE : new Primitive(idGenerator.getString(rule))));
                    continue;
                case RelativeLayout.BELOW:
                    arrayList.add(new Attribute(Attributes.View.Below, rule == 0 ? Null.INSTANCE : new Primitive(idGenerator.getString(rule))));
                    continue;
                case RelativeLayout.ALIGN_BASELINE:
                    arrayList.add(new Attribute(Attributes.View.AlignBaseline, rule == 0 ? Null.INSTANCE : new Primitive(rule)));
                    continue;
                case RelativeLayout.ALIGN_LEFT:
                    arrayList.add(new Attribute(Attributes.View.AlignLeft, rule == 0 ? Null.INSTANCE : new Primitive(rule)));
                    continue;
                case RelativeLayout.ALIGN_PARENT_BOTTOM:
                    arrayList.add(new Attribute(Attributes.View.AlignParentBottom, rule == 0 ? Null.INSTANCE : new Primitive(rule)));
                    continue;
                case RelativeLayout.ALIGN_PARENT_TOP:
                    arrayList.add(new Attribute(Attributes.View.AlignParentTop, rule == 0 ? Null.INSTANCE : new Primitive(rule)));
                    continue;
                case RelativeLayout.ALIGN_PARENT_END:
                    arrayList.add(new Attribute(Attributes.View.AlignParentEnd, rule == 0 ? Null.INSTANCE : new Primitive(rule)));
                    continue;
                case RelativeLayout.ALIGN_PARENT_START:
                    arrayList.add(new Attribute(Attributes.View.AlignParentStart, rule == 0 ? Null.INSTANCE: new Primitive(rule)));
                    continue;
                case RelativeLayout.ALIGN_PARENT_LEFT:
                    arrayList.add(new Attribute(Attributes.View.AlignParentLeft, rule == 0 ? Null.INSTANCE : new Primitive(rule)));
                    continue;
                case RelativeLayout.ALIGN_PARENT_RIGHT:
                    arrayList.add(new Attribute(Attributes.View.AlignParentRight, rule == 0 ? Null.INSTANCE: new Primitive(rule)));
                    continue;
                case RelativeLayout.CENTER_IN_PARENT:
                    arrayList.add(new Attribute(View.CenterInParent, rule == 0 ? Null.INSTANCE: new Primitive(rule)));
                    continue;
                case RelativeLayout.CENTER_HORIZONTAL:
                    arrayList.add(new Attribute(View.CenterHorizontal, rule == 0 ? Null.INSTANCE: new Primitive(rule)));
                    continue;
                case RelativeLayout.CENTER_VERTICAL:
                    arrayList.add(new Attribute(View.CenterVertical, rule == 0 ? Null.INSTANCE: new Primitive(rule)));
                    continue;
            }
        }
        return arrayList;
    }
}