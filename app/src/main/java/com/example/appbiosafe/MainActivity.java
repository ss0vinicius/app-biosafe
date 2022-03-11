package com.example.appbiosafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private boolean supervisionar;
    private TarefaAssincronaSupervisao tarefaSupervisao;


    @Override
    protected void finalize(){
        this.tarefaSupervisao.parar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        supervisaoAssincrona(supervisionar);
        this.supervisionar = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void supervisaoAssincrona(boolean supervisionar){ //função precisa receber se está em ciclo ou não
        // dispara a tarefa de supervisão
            tarefaSupervisao = new TarefaAssincronaSupervisao();
            tarefaSupervisao.execute(new String[]{""}); //vai ficar rodando? tomar cuidado para não ficar eternamente rodand
    }

    public void cicloNormal(View view) {
        // dispara a tarefa de controle

        if(this.tarefaSupervisao.getDados().getLedOnCicleStatus().equalsIgnoreCase("ON")){

        }
        else {
            String comandos = "";
            comandos += "?button=Normal";

            TarefaAssincronaControle tarefaControle = new TarefaAssincronaControle();
            tarefaControle.execute(new String[]{comandos});
        }
    }
    public void cicloRapido(View view) {
        // dispara a tarefa de controle
        if(this.tarefaSupervisao.getDados().getLedOnCicleStatus().equalsIgnoreCase("ON")){

        }
        else {
            String comandos = "";
            comandos += "?button=Rapid";

            TarefaAssincronaControle tarefaControle = new TarefaAssincronaControle();
            tarefaControle.execute(new String[]{comandos});
        }
    }

    public void cicloAbortar(View view) {
        // dispara a tarefa de controle
        String comandos = "";
        comandos += "?button=Abort";

        TarefaAssincronaControle tarefaControle = new TarefaAssincronaControle();
        tarefaControle.execute(new String[]{comandos});
    }

    private class TarefaAssincronaControle extends AsyncTask<String, Void, String> {
        private Dados dados = new Dados();

        // dispara a requisição
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) ( new URL(this.dados.getIp() + "controle" + params[0])).openConnection(); //chamar ip global
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                StringBuffer buffer = new StringBuffer();
                InputStream is = con.getInputStream();
                is.close();
                con.disconnect();
                return "Sucesso na comunicação";
            } catch (IOException e) {
                e.printStackTrace();
                return "Erro na comunicação";
            }
        }

        // trata os dados retornados
        @Override
        protected void onPostExecute(String retorno) {
            super.onPostExecute(retorno);
            EditText edTxPainel = (EditText) findViewById(R.id.edTxPainel);
            edTxPainel.setText(retorno);
        }
    }
    private class TarefaAssincronaSupervisao extends AsyncTask<String, Void, String> {
        // dispara a requisição

        private boolean supervisionar = true;

        Dados dados = new Dados();

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection con = null;

            while (supervisionar == true) {
                try {
                    con = (HttpURLConnection) (new URL(this.dados.getIp() + "supervisao")).openConnection(); //chamar ip global
                    con.setRequestMethod("GET");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.connect();
                    StringBuffer buffer = new StringBuffer();
                    InputStream is = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    while ((line = br.readLine()) != null)
                        buffer.append(line + "\r\n");

                    atualiza(); //Log.e("AAA:",stringData);

                    is.close(); //é aqui que paramos de rodar?
                    con.disconnect();
                    return buffer.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Erro na comunicação";
                }
            }

            return "";
        }


        public void atualizaDados(String[] dadosSeparados){

            for (int i = 0; i < dadosSeparados.length; i++)
            {
                String[] argumento = dadosSeparados[i].split(";");

                this.dados.setLedStopStatus(argumento[0]);
                this.dados.setLedOnCicleStatus(argumento[1]);
                this.dados.setLedStandByStatus(argumento[2]);
                this.dados.setRelayHeatStatus(argumento[3]);
                this.dados.setRelayLampStatus(argumento[4]);
                this.dados.setDoorStatus(argumento[5]);
                this.dados.setTemperature(argumento[6]);
                this.dados.setTimeRemaing(argumento[7]);
            }
        }

        public void parar(){
            this.supervisionar = false;
        }

        public Dados getDados(){
            return this.dados;
        }

        // trata os dados retornados
        @Override
        protected void onPostExecute(String retorno) {
            EditText edTxPainel = (EditText) findViewById(R.id.edTxPainel);
            if (retorno.equals("Erro na comunicação"))
            {
                edTxPainel.setText(retorno);
                return;
            }
            edTxPainel.setText("Sucesso na comunicação");
            String[] dadosSeparados = retorno.split("\n");
            this.atualizaDados(dadosSeparados);
            super.onPostExecute(retorno);
        }


        public void atualiza(){
            EditText edTxStopStatus = (EditText) findViewById(R.id.edTxStopStatus);
            edTxStopStatus.setText(this.dados.getLedStopStatus());
            EditText edTxOnCicleStatus = (EditText) findViewById(R.id.edTxOnCicleStatus);
            edTxOnCicleStatus.setText(this.dados.getLedOnCicleStatus());
            EditText edTxStandBy = (EditText) findViewById(R.id.edTxStandBy);
            edTxStandBy.setText(this.dados.getLedStandByStatus());
            EditText edTxRelayHeatStatus = (EditText) findViewById(R.id.edTxRelayHeatStatus);
            edTxRelayHeatStatus.setText(this.dados.getRelayHeatStatus());
            EditText edTxRelayLampStatus = (EditText) findViewById(R.id.edTxRelayLampStatus);
            edTxRelayLampStatus.setText(this.dados.getRelayLampStatus());
            EditText edTxDoorStatus = (EditText) findViewById(R.id.edTxDoorStatus);
            edTxDoorStatus.setText(this.dados.getDoorStatus());
            EditText edTxTemperature = (EditText) findViewById(R.id.edTxTemperature);
            edTxTemperature.setText(this.dados.getTemperature());
            EditText edTxTimeRemaing = (EditText) findViewById(R.id.edTxTimeRemaing);
            edTxTimeRemaing.setText(this.dados.getTimeRemaing());

            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EditText edTxStopStatus = (EditText) findViewById(R.id.edTxStopStatus);
                    edTxStopStatus.setText(this.dados.getLedStopStatus());
                    EditText edTxOnCicleStatus = (EditText) findViewById(R.id.edTxOnCicleStatus);
                    edTxOnCicleStatus.setText(this.dados.getLedOnCicleStatus());
                    EditText edTxStandBy = (EditText) findViewById(R.id.edTxStandBy);
                    edTxStandBy.setText(this.dados.getLedStandByStatus());
                    EditText edTxRelayHeatStatus = (EditText) findViewById(R.id.edTxRelayHeatStatus);
                    edTxRelayHeatStatus.setText(this.dados.getRelayHeatStatus());
                    EditText edTxRelayLampStatus = (EditText) findViewById(R.id.edTxRelayLampStatus);
                    edTxRelayLampStatus.setText(this.dados.getRelayLampStatus());
                    EditText edTxDoorStatus = (EditText) findViewById(R.id.edTxDoorStatus);
                    edTxDoorStatus.setText(this.dados.getDoorStatus());
                    EditText edTxTemperature = (EditText) findViewById(R.id.edTxTemperature);
                    edTxTemperature.setText(this.dados.getTemperature());
                    EditText edTxTimeRemaing = (EditText) findViewById(R.id.edTxTimeRemaing);
                    edTxTimeRemaing.setText(this.dados.getTimeRemaing());

                }
            });*/
        }
    }
}