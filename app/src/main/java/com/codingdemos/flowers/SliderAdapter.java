package com.codingdemos.flowers;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<Integer> color;
    private List<String> colorName;
    private List<String> colorImage;

    public SliderAdapter(Context context, List<Integer> color, List<String> colorName, List<String> colorImage) {
        this.context = context;
        this.color = color;
        this.colorName = colorName;
        this.colorImage = colorImage;
    }

    @Override
    public int getCount() {
        return color.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);

        textView.setText(colorName.get(position));
        relativeLayout.setBackgroundColor(color.get(position));

        ImageView imageView = (ImageView) view.findViewById(R.id.item_slide_image_iv);
        Glide.with(context)
                .load(
                        colorImage.get(position)
                        //model.getImage()
//                        .replace(" ", "%20")
                )
//                .placeholder(R.drawable.hagia_sophia)
//                .error(R.drawable.hagia_sophia)
                .into(imageView);


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
