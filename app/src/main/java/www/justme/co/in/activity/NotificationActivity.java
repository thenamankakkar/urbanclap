package www.justme.co.in.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.cscodetech.townclap.R;
import www.justme.co.in.model.Noti;
import www.justme.co.in.model.NotificationDatum;
import www.justme.co.in.model.User;
import www.justme.co.in.retrofit.APIClient;
import www.justme.co.in.retrofit.GetResult;
import www.justme.co.in.utils.CustPrograssbar;
import www.justme.co.in.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class NotificationActivity extends BasicActivity implements GetResult.MyListener {


    @BindView(R.id.lvl_myorder)
    LinearLayout lvlMyOrder;
    @BindView(R.id.txt_notfound)
    TextView txtNotFound;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotFound;
    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;
    Boolean data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Notification");

        sessionManager = new SessionManager(NotificationActivity.this);
        data = sessionManager.getBooleanData("login");
        if (data == true){
            user = sessionManager.getUserDetails("");
            custPrograssbar = new CustPrograssbar();
            getNotification();
        }else {
            Toast.makeText(getApplicationContext(), "You are not Logged In", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(NotificationActivity.this, LoginActivity.class);
            finish();
            startActivity(login);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void setNotiList(LinearLayout lnrView, List<NotificationDatum> list) {
        lnrView.removeAllViews();


        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(NotificationActivity.this);

            View view = inflater.inflate(R.layout.custome_noti, null);

            TextView txtTitel = view.findViewById(R.id.txt_titel);
            TextView txtDatetime = view.findViewById(R.id.txt_datetime);
            TextView txtDescption = view.findViewById(R.id.txt_descption);
            String date = parseDateToddMMyyyy(list.get(i).getDatetime());

            txtTitel.setText("  " + list.get(i).getTitle() + "  ");
            txtDatetime.setText("" + date);
            txtDescption.setText("" + list.get(i).getDescription());
            lnrView.addView(view);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void getNotification() {
        custPrograssbar.prograssCreate(NotificationActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getNote(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Noti notification = gson.fromJson(result.toString(), Noti.class);
                if (notification.getResult().equalsIgnoreCase("true")) {

                    setNotiList(lvlMyOrder, notification.getNotificationData());
                } else {
                    lvlNotFound.setVisibility(View.VISIBLE);
                    txtNotFound.setText("" + notification.getResponseMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}