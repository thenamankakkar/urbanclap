package www.justme.co.in.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.townclap.R;
import www.justme.co.in.adepter.TrazectionAdapter;
import www.justme.co.in.model.User;
import www.justme.co.in.model.Wallet;
import www.justme.co.in.retrofit.APIClient;
import www.justme.co.in.retrofit.GetResult;
import www.justme.co.in.utils.CustPrograssbar;
import www.justme.co.in.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class WalletActivity extends BasicActivity implements GetResult.MyListener {
    @BindView(R.id.txt_wallet)
    TextView txtWallet;
    @BindView(R.id.txt_income)
    TextView txtIncome;
    @BindView(R.id.txt_expence)
    TextView txtExpence;
    @BindView(R.id.recycle_trazection)
    RecyclerView recycleTrazection;

    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;
    Boolean data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setTitle("My Wallet");
        ButterKnife.bind(this);

        recycleTrazection.setLayoutManager(new LinearLayoutManager(this));
        recycleTrazection.setItemAnimator(new DefaultItemAnimator());


        sessionManager = new SessionManager(WalletActivity.this);
        data = sessionManager.getBooleanData("login");
        if (data == true){
            user = sessionManager.getUserDetails("");
            custPrograssbar = new CustPrograssbar();
            getWallet();
        }else {
            Toast.makeText(getApplicationContext(), "You are not Logged In", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(WalletActivity.this, LoginActivity.class);
            finish();
            startActivity(login);
        }


    }

    private void getWallet() {
        custPrograssbar.prograssCreate(WalletActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getWallet(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Wallet wallet = gson.fromJson(result.toString(), Wallet.class);
                if (wallet.getResult().equalsIgnoreCase("true")) {
                    txtWallet.setText(sessionManager.getStringData(SessionManager.currency) + wallet.getWallets());
                    txtExpence.setText(sessionManager.getStringData(SessionManager.currency) + wallet.getDebittotal());
                    txtIncome.setText(sessionManager.getStringData(SessionManager.currency) + wallet.getCredittotal());
                    sessionManager.setStringData(SessionManager.wallet,wallet.getWallets());
                    TrazectionAdapter adapter = new TrazectionAdapter(this, wallet.getWalletitem());
                    recycleTrazection.setAdapter(adapter);
                }
            }

        } catch (Exception e) {
            Log.e("Error", "" + e.toString());


        }

    }
}