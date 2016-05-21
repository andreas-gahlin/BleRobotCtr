package com.example.andre.robotprojekt;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.util.Scanner;

/**
 * This class makes http request to my server witch contains a database and put the data user fill in
 * to the database
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class BackgroundTask  extends AsyncTask<String, Void, String> {
    String REG_URL, LOG_URL;
    String Name, Password, Method, City;
    Context _Context;
    public String DATABASE;

    private static final int MSG_PROGRESS = 201;
    private static final int MSG_DISMISS = 202;
    private static final int MSG_CLEAR = 301;
    private ProgressDialog _progressDialog;

    /**************************************************************************
     * VARIABLE NAME : _Handler
     * DESCRIPTION   : Sets up a progressdialog with modifyed text
     * NOTE          :
     **************************************************************************/
    private Handler _Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MSG_PROGRESS:
                    _progressDialog.setMessage((String) msg.obj);
                    _progressDialog.show();
                    break;
                case MSG_DISMISS:
                    _progressDialog.hide();
                    break;
                case MSG_CLEAR:
                    _progressDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    /**************************************************************************
     * FUNCTION NAME : CustomSurfaceView
     * DESCRIPTION   : Everytime this windows start we initilaze this window
     * NOTE          :
     **************************************************************************/
    public BackgroundTask(Context context) {
        _Context = context;
    }

    /**************************************************************************
     * FUNCTION NAME : onPreExecute
     * DESCRIPTION   : Is a function before Execute the specific task
     * NOTE          : The URL and the database is (Andreas) These shall not be used
     *                 other then when the application will be examination
     **************************************************************************/
    @Override
    protected void onPreExecute() {
        _progressDialog = new ProgressDialog(_Context);
        _progressDialog.setIndeterminate(true);
        _progressDialog.setCancelable(false);
        REG_URL = "http://agqtech.nu/UserRegistration.php";
        LOG_URL = "http://agqtech.nu/GetRegisterUser.php";
        _Handler.sendMessage(Message.obtain(null, MSG_PROGRESS, _Context.getString(R.string.Pre_Conn)));
        super.onPreExecute();
    }

    /**************************************************************************
     * FUNCTION NAME : doInBackground
     * DESCRIPTION   : To put of the heavy work from UI we use a bakground task wich sends
     *                 http request to the database and returns the readback from the server
     * NOTE          : if everything was successfull a return true is returned
     **************************************************************************/
    @Override
    protected String doInBackground(String... params) {
        Name = params[0];
        Password = params[1];
        Method = params[2];
        /**************************************************************************
         * DESCRIPTION   : if method is register we send the data to the database
         * NOTE          : when data was succsessfull stored in the database we get a readback of true : false
         **************************************************************************/
        if(Method.equals("Register")) {
            _Handler.sendMessage(Message.obtain(null, MSG_PROGRESS, _Context.getString(R.string.Database_Conn)));
            City = params[3];
            try {
                URL url = new URL(REG_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_output =
                        URLEncoder.encode("usersname", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8") + "&" +
                                URLEncoder.encode("userpassword", "UTF-8") + "=" + URLEncoder.encode(Password, "UTF-8") + "&" +
                                URLEncoder.encode("usercity", "UTF-8") + "=" + URLEncoder.encode(City, "UTF-8");
                bufferedWriter.write(data_output);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                String line = scanner.nextLine();
                inputStream.close();
                httpURLConnection.disconnect();
                return line;
            }
            catch (MalformedInputException e) {
                _Handler.sendMessage(Message.obtain(null, MSG_CLEAR, null ));
                e.printStackTrace();
            }
            catch (IOException e) {
                _Handler.sendMessage(Message.obtain(null, MSG_CLEAR, null ));
                e.printStackTrace();
            }
        }

        /**************************************************************************
         * DESCRIPTION   : if method is register we send the data to the database
         * NOTE          : when data was succsessfull stored in the database we get a read back of true : false
         **************************************************************************/
        if(Method.equals("Login")) {
            _Handler.sendMessage(Message.obtain(null, MSG_PROGRESS, _Context.getString(R.string.Database_Conn)));
            try {
                URL url = new URL(LOG_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_output =
                        URLEncoder.encode("usersname", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8") + "&" +
                        URLEncoder.encode("userpassword", "UTF-8") + "=" + URLEncoder.encode(Password, "UTF-8");
                bufferedWriter.write(data_output);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                String line = scanner.nextLine();
                inputStream.close();
                httpURLConnection.disconnect();

                return line;
            }
            catch (MalformedInputException e) {
                _Handler.sendMessage(Message.obtain(null, MSG_CLEAR, null ));
                e.printStackTrace();
            }
            catch (IOException e) {
                _Handler.sendMessage(Message.obtain(null, MSG_CLEAR, null ));
                e.printStackTrace();
            }
        }
        return "Connection to server timed out!";
    }

    /**************************************************************************
     * FUNCTION NAME : onPostExecute
     * DESCRIPTION   : Readback from the server witch specify if the user typed the right name and password
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onPostExecute(String CallbackFromServer) {
        _Handler.sendMessage(Message.obtain(null, MSG_CLEAR, null ));
        DATABASE = CallbackFromServer;
        if(CallbackFromServer.equals("User Register!"))
            Toast.makeText(_Context.getApplicationContext(), CallbackFromServer, Toast.LENGTH_SHORT).show();
        else if(CallbackFromServer.equals("Username might be busy!"))
            Toast.makeText(_Context.getApplicationContext(), CallbackFromServer, Toast.LENGTH_SHORT).show();
        if(CallbackFromServer.equals("true"))
            Toast.makeText(_Context.getApplicationContext(), R.string.LoginSucsessFull, Toast.LENGTH_SHORT).show();
        else if(CallbackFromServer.equals("false"))
            Toast.makeText(_Context.getApplicationContext(), R.string.Login_Failed, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(_Context.getApplicationContext(), R.string.No_internet_conn, Toast.LENGTH_SHORT).show();
    }
}
