package com.example.bovink.retrofitexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.bovink.retrofitexample.api.API;
import com.example.bovink.retrofitexample.model.Repo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView output;
    private List<Repo> repos;

    private static int REQUEST_TYPE = -1;

    private final static int REQUEST_SYNCHRONOUS = 0;
    private final static int REQUEST_ASYNCHRONOUS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.tv_output);

        REQUEST_TYPE = REQUEST_SYNCHRONOUS;
        request();
    }

    private void request() {
        if (REQUEST_TYPE == REQUEST_SYNCHRONOUS) {
            synchronousRequest();
        } else if (REQUEST_TYPE == REQUEST_ASYNCHRONOUS){
            asynchronousRequest();
        }
    }

    private void asynchronousRequest() {
        API api = ServiceGenerator.createService(API.class);

        Call<List<Repo>> call = api.getRepoList("bovink", "1", "5");

        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                repos = response.body();
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {

            }
        });
    }

    private void synchronousRequest() {
        SynchronousThread thread = new SynchronousThread();
        thread.start();
    }

    private class SynchronousThread extends Thread {

        @Override
        public void run() {
            API api = ServiceGenerator.createService(API.class);
            // 使用另外一种方式输入查询参数
            Map<String, String> options = new HashMap<>();
            options.put("page", "1");
            options.put("per_page", "5");

            Call<List<Repo>> call = api.getRepoList("bovink", options);
            try {
                Response<List<Repo>> response = call.execute();
                repos = response.body();
                handler.sendEmptyMessage(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            StringBuilder stringBuilder = new StringBuilder();
            if (REQUEST_TYPE == REQUEST_SYNCHRONOUS) {
                stringBuilder.append("from synchronous")
                        .append("\n");
            } else if (REQUEST_TYPE == REQUEST_ASYNCHRONOUS){
                stringBuilder.append("from asynchronous")
                        .append("\n");
            }
            for (Repo repo : repos) {
                stringBuilder.append(repo.getName())
                        .append("\n");
            }
            output.setText(stringBuilder.toString());
        }
    };
}
