package www.justme.co.in.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cscodetech.townclap.R;
import www.justme.co.in.model.User;
import www.justme.co.in.utils.CustPrograssbar;
import www.justme.co.in.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BasicActivity {
    SessionManager sessionManager;
    User user;

    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_mob)
    TextView txtMob;
    @BindView(R.id.lvl_edit)
    LinearLayout lvlEdit;
    @BindView(R.id.lvl_menu1)
    LinearLayout lvlMenu1;
    @BindView(R.id.lvl_menu2)
    LinearLayout lvlMenu2;
    @BindView(R.id.lvl_menu3)
    LinearLayout lvlMenu3;

    @BindView(R.id.lvl_menu4)
    LinearLayout lvlMenu4;
    @BindView(R.id.lvl_menu5)
    LinearLayout lvlMenu5;
    @BindView(R.id.lvl_menu6)
    LinearLayout lvlMenu6;
    @BindView(R.id.lvl_logot)
    LinearLayout lvlLogot;


    CustPrograssbar custPrograssbar;
    Boolean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("My Profile");
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(SettingActivity.this);
        data = sessionManager.getBooleanData("login");
        if (data == true){
            user = sessionManager.getUserDetails("");
        }else {
            Toast.makeText(getApplicationContext(), "You are not Logged In", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(SettingActivity.this, LoginActivity.class);
            finish();
            startActivity(login);
        }


    }

    @OnClick({R.id.lvl_menu1, R.id.lvl_menu2, R.id.lvl_menu3, R.id.lvl_menu4, R.id.lvl_menu5, R.id.lvl_menu6, R.id.lvl_logot, R.id.lvl_edit, R.id.lvl_contec, R.id.lvl_privacy, R.id.lvl_trams})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvl_menu1:

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.cscodetech.partner")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.cscodetech.partner")));
                }
                break;
            case R.id.lvl_menu2:
                startActivity(new Intent(SettingActivity.this, BookingActivity.class));

                break;

            case R.id.lvl_menu3:
                startActivity(new Intent(SettingActivity.this, AboutsActivity.class));

                break;
            case R.id.lvl_contec:
                startActivity(new Intent(SettingActivity.this, ContectusActivity.class));

                break;
            case R.id.lvl_menu4:

                break;
            case R.id.lvl_menu5:
                startActivity(new Intent(SettingActivity.this, ReferlActivity.class));
                break;
            case R.id.lvl_menu6:
                startActivity(new Intent(SettingActivity.this, WalletActivity.class));
                break;
            case R.id.lvl_logot:
                sessionManager.logoutUser();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.lvl_edit:
                startActivity(new Intent(SettingActivity.this, EditProfileActivity.class));

                break;
            case R.id.lvl_privacy:
                startActivity(new Intent(SettingActivity.this, PrivecyPolicyActivity.class));

                break;
            case R.id.lvl_trams:
                startActivity(new Intent(SettingActivity.this, TramsAndConditionActivity.class));

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager != null) {
            user = sessionManager.getUserDetails("");

            txtMob.setText("" + user.getMobile());
            txtName.setText("" + user.getName());
        }
    }
}