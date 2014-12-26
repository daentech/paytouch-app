package uk.dan_gilbert.paytouch.ui;

import android.graphics.Typeface;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by dangilbert on 26/12/14.
 */
public class TypographyFacade {

    private static Typeface myriadProTypeface;
    private static Typeface myriadProItalicTypeface;
    private static Typeface myriadProBoldTypeface;

    public static ButterKnife.Action<TextView> typographyAction = new ButterKnife.Action<TextView>() {
        @Override
        public void apply(TextView view, int index) {
            if (myriadProTypeface == null) {
                myriadProTypeface = Typeface.createFromAsset(view.getContext().getAssets(), "font/MyriadPro-Regular.otf");
                myriadProItalicTypeface = Typeface.createFromAsset(view.getContext().getAssets(), "font/MyriadPro-It.otf");
                myriadProBoldTypeface = Typeface.createFromAsset(view.getContext().getAssets(), "font/MyriadPro-Bold.otf");
            }

            if (view.getTypeface().isBold()) {
                view.setTypeface(myriadProBoldTypeface);
            } else if (view.getTypeface().isItalic()) {
                view.setTypeface(myriadProItalicTypeface);
            } else {
                view.setTypeface(myriadProTypeface);
            }

        }
    };

}
