package br.com.thecharles.hihealth.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;

import br.com.thecharles.hihealth.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.facebook.FacebookSdk.getClientToken;

public class DataFragment extends Fragment{

    private static final String TAG = DataFragment.class.getSimpleName();

    private SwipeRefreshLayout refreshLayout;

    TextView countdownText;

    CountDownTimer countDownTimer;
    long timeLeftInMilleSeconds = 30000;
    boolean timeRunning;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_data, container, false);

        refreshLayout = v.findViewById(R.id.swipe);
        refreshLayout.setOnRefreshListener(OnRefreshListener());

        FloatingActionButton fab = v.findViewById(R.id.fab_warning);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

                //Pegando o dialog
                View mView = getLayoutInflater().inflate(R.layout.dialog_alert, null);

                //Buttons do Dialog
                Button mEnviar =  mView.findViewById(R.id.btnEnviar);
                Button mCancelar =  mView.findViewById(R.id.btnCancelar);


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                mEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Alerta enviada com sucesso", Toast.LENGTH_SHORT).show();

                        /** TESTE REQUEST FMC */

                       sendData();

//                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//
//                        //Pegando o dialog
//                        View mView = getLayoutInflater().inflate(R.layout.dialog_sensor, null);
//
//
//                        //Button do Dialog
//                        Button mBem = mView.findViewById(R.id.btnBem);
////                        Button mMal = mView.findViewById(R.id.btnMal);
//                        countdownText = mView.findViewById(R.id.countdown_text);
//
//                        mBuilder.setView(mView);
//                        final AlertDialog dialog = mBuilder.create();
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        dialog.show();
//
//                        //Metodo para inicializar o temporizador
//                        //startTimer();
//                        startTimer();
//
//
//
//
//                        mBem.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Toast.makeText(getActivity(), "Alerta Emitido", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                        });

//                        mMal.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Toast.makeText(getActivity(), "Alerta Cancelado", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                        });

                    }
                });

                mCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return  v;
    }

    private void requestPostFCM(String json) throws IOException{

        OkHttpClient client;
        client = new OkHttpClient();

        String url = "https://fcm.googleapis.com/fcm/send";

        Request.Builder builder = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=AIzaSyDhbrMURhxQJBqZOSRm-7kGfUckEEiNpXg");

        builder.url(url);

        MediaType mediaType =
                MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(mediaType, json);
        builder.post(body);

        Request request = builder.build();

        Response response = client.newCall(request).execute();

        String jsonDeResposta = response.body().string();

//            return jsonDeResposta;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error sending firebase app instance token to app server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) {
                    throw new IOException
                            ("Firebase app instance token to server status " + response);
                }

                Log.i(TAG, "Firebase app instance token has been sent to app server "
                        +responseBody.string());
            }
        });

    }

    private String getFCMDataMessage() {

        Item item = getClientTokenAndData();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("token", item.getToken());

        JsonObject itemInfo = new JsonObject();
        itemInfo.addProperty("itemName", item.getName());
        itemInfo.addProperty("itemPrice", item.getPrice());
        itemInfo.addProperty("location", item.getLocation());

        jsonObj.add("data", itemInfo);

        JsonObject msgObj = new JsonObject();
        msgObj.add("message", jsonObj);

        Log.d(TAG,"data  message " + msgObj.toString());

        return msgObj.toString();
    }

    private String getFCMNotificationMessage(String title, String msg) {
        JsonObject jsonObj = new JsonObject();
        // client registration key is sent as token in the message to FCM server
        jsonObj.addProperty("token", getClientToken());

        JsonObject notification = new JsonObject();
        notification.addProperty("body", msg);
        notification.addProperty("title", title);
        jsonObj.add("notification", notification);

        JsonObject message = new JsonObject();
        message.add("message", jsonObj);

        Log.d(TAG,"notification message " + message.toString());

        return message.toString();
    }

    private void sendData() throws IOException {
        requestPostFCM(getFCMDataMessage());
    }


    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilleSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilleSeconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timeRunning = true;
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMilleSeconds / 60000;
        int seconds = (int) timeLeftInMilleSeconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";

        if (seconds < 10 ) timeLeftText += "0";

        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Fragment childAFragment = new ChildAFragment();
                Fragment childBFragment = new ChildBFragment();
                Fragment childCFragment = new ChildCFragment();
                Fragment childDFragment = new ChildDFragment();

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

//        transaction.replace(R.id.child_fragment_container, childFragment);
                transaction.replace(R.id.child_fragment_a_container, childAFragment);
                transaction.replace(R.id.child_fragment_b_container, childBFragment);
                transaction.replace(R.id.child_fragment_c_container, childCFragment);
                transaction.replace(R.id.child_fragment_d_container, childDFragment);
                transaction.commit();

                refreshLayout.setRefreshing(false);
            }
        };
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        Fragment childFragment = new ChildFragment();
        Fragment childAFragment = new ChildAFragment();
        Fragment childBFragment = new ChildBFragment();
        Fragment childCFragment = new ChildCFragment();
        Fragment childDFragment = new ChildDFragment();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

//        transaction.replace(R.id.child_fragment_container, childFragment);
        transaction.replace(R.id.child_fragment_a_container, childAFragment);
        transaction.replace(R.id.child_fragment_b_container, childBFragment);
        transaction.replace(R.id.child_fragment_c_container, childCFragment);
        transaction.replace(R.id.child_fragment_d_container, childDFragment);
        transaction.commit();


    }


    public static DataFragment newInstance() {
        return new DataFragment();
    }


    public void onDialog(View view) {

    }
}
