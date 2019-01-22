package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

/**
 * Created by nande on 2/13/2017.
 */

public class AboutDialog extends DialogFragment {
    TextView mdialog_tv;

    public AboutDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.about_dialog,null);
        mdialog_tv=(TextView)v.findViewById(R.id.aboutdialog_tv);
        mdialog_tv.setText(R.string.about);
        mdialog_tv.setTextColor(Color.RED);
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("About this Application").create();
    }
}
