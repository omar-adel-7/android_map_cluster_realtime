package modules.general.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.map_realtime.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import modules.general.ui.utils.general_listeners.ITitleListener;

public class CustomTitle extends LinearLayout {


    @BindView(R.id.customtitle_txt_title)
    TextView txtTitle;
    ITitleListener listenerMain;

    public CustomTitle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_title, this);
        ButterKnife.bind(this);
    }


    public void initalizeView(Context context, String title, ITitleListener titleListener) {
        txtTitle.setText(title);
        listenerMain = titleListener;

    }

    public void updateTitle(String title) {
        txtTitle.setText(title);
    }

    public void hideCustomTitle() {
        setVisibility(GONE);
    }

    public void showCustomTitle() {
        setVisibility(VISIBLE);
    }





}