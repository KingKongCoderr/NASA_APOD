package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by nande on 2/16/2017.
 */

public class SubmitDialog extends DialogFragment {
    EditText mname_et;
    String email_subject="";
    ImageView mnasa_iv;

    public SubmitDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        View v=getActivity().getLayoutInflater().inflate(R.layout.submit_dialog,null);


        mname_et=(EditText)v.findViewById(R.id.name_et);
        if(email_subject.length()==0){
            mname_et.setError("Enter your name");
        }
        mnasa_iv=(ImageView)v.findViewById(R.id.nasa_iv);
        Glide.with(getContext()).load(R.drawable.nasa_real).asBitmap().into(mnasa_iv);





        return    builder.setView(v).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    email_subject = mname_et.getText().toString();

                        Intent intent=new Intent(Intent.ACTION_SENDTO);
                        intent.setType("text/plain");
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        String[] addresses={"nemiroff@mtu.edu","bonnell@grossc.gsfc.nasa.gov"};
                        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                        String email_body="";
                        intent.putExtra(Intent.EXTRA_TEXT,email_body);
                        intent.putExtra(Intent.EXTRA_SUBJECT,email_subject+" APOD submission");
                        intent= Intent.createChooser(intent,"Email");
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(intent);
                        }

                }catch (Exception e){
                    e.printStackTrace();
                }




            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SubmitDialog.this.getDialog().cancel();
                    }
                }).setTitle("Submit Your APOD Pictures").create();
    }
    private void requestPermission() {


            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.INTERNET},1);
    }

}
