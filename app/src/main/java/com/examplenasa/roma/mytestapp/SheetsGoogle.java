package com.examplenasa.roma.mytestapp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.sheets.v4.SheetsScopes;

import com.google.api.services.sheets.v4.model.*;

import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;



public class SheetsGoogle extends Activity
        implements EasyPermissions.PermissionCallbacks, View.OnClickListener {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button mButtonRead, mButtonWrite, mButtonBackToWrite;
    private EditText mEditUniversity, mEditUser, mEditTeacher, mEditComment;
    private RatingBar mRatingBar1, mRatingBar2, mRatingBar3, mRatingBar4, mRatingBar5;

    ProgressDialog mProgress;

    final String LOG_TAG = "SheetLog";

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "starikhottabichrom";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheets_google);


        mButtonRead = (Button) findViewById(R.id.button_read_from_sheet);
        mButtonRead.setOnClickListener(this);

        mButtonWrite = (Button) findViewById(R.id.button_write_to_sheet);
        mButtonWrite.setOnClickListener(this);

        mButtonBackToWrite = (Button) findViewById(R.id.button_back_to_write);
        mButtonBackToWrite.setOnClickListener(this);

        mEditUniversity = (EditText)findViewById(R.id.edit_text_university);
        mEditUser = (EditText) findViewById(R.id.edit_text_username);
        mEditTeacher = (EditText) findViewById(R.id.edit_text_teacher_name);
        mEditComment = (EditText) findViewById(R.id.edit_text_comment);

        mRatingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        mRatingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        mRatingBar3 = (RatingBar) findViewById(R.id.ratingBar3);
        mRatingBar4 = (RatingBar) findViewById(R.id.ratingBar4);
        mRatingBar5 = (RatingBar) findViewById(R.id.ratingBar5);

        mOutputText = (TextView) findViewById(R.id.text_inf_google_sheet);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Sheets API ...");

 ;

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());


        getResultsFromApi();

        Log.d(LOG_TAG, "On Create");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_read_from_sheet:
                mButtonRead.setEnabled(false);
                mOutputText.setText("");
                getResultsFromApi();
                mButtonRead.setEnabled(true);
                Log.d(LOG_TAG, "Read button");

                findViewById(R.id.input_layout).setVisibility(View.GONE);
                findViewById(R.id.text_inf_google_sheet).setVisibility(View.VISIBLE);
                findViewById(R.id.button_write_to_sheet).setVisibility(View.GONE);
                findViewById(R.id.button_back_to_write).setVisibility(View.VISIBLE);
                break;

            case R.id.button_write_to_sheet:


                String university = mEditUniversity.getText().toString();
                String userName = mEditUser.getText().toString();
                String teacherName = mEditTeacher.getText().toString();
                float rating1 = mRatingBar1.getRating();
                float rating2 = mRatingBar2.getRating();
                float rating3 = mRatingBar3.getRating();
                float rating4 = mRatingBar4.getRating();
                float rating5 = mRatingBar5.getRating();



                String comment = mEditComment.getText().toString();
                if(comment.trim().length() == 0){
                    comment = "No comment";
                }

                boolean key = false;
                while (key == false) {
                    if (userName.trim().length() == 0 || university.trim().length() == 0 || teacherName.trim().length() == 0) {
                        Toast.makeText(this, "Введіть будь ласка навчальний заклад, ваше імя і імя викладача ", Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, "Input data pls");
                        break;
                    } else {

                        OneReview a = new OneReview(university, userName, teacherName, rating1, rating2, rating3, rating4, rating5, comment);
                        setDataRuquestToApi(a);
                        Toast.makeText(this, "Дані записані успішно", Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, "Write button");

                        key = true;

                mEditUniversity.setText("");
                mEditUser.setText("");
                mEditTeacher.setText("");
                mRatingBar1.setRating(0);
                mRatingBar2.setRating(0);
                mRatingBar3.setRating(0);
                mRatingBar4.setRating(0);
                mRatingBar5.setRating(0);
                mEditComment.setText("");
                    }
                }


                break;

            case R.id.button_back_to_write:
                findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.text_inf_google_sheet).setVisibility(View.GONE);
                findViewById(R.id.button_write_to_sheet).setVisibility(View.VISIBLE);
                findViewById(R.id.button_back_to_write).setVisibility(View.GONE);
                break;

    }
        }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText("No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    private void setDataRuquestToApi(OneReview oneReview) {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText("No network connection available.");
        } else {
            new MakeWriteRequestTask(mCredential).execute(oneReview);
        }
    }


    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                SheetsGoogle.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }






    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("MyTestAPI")
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }



        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            String spreadsheetId = "1J03H-yudUHAA2gGvgYOAPvQNzwRNTn2faYjaqHo-Dc4";
            String range = "Sheet!A2:J20";

            List<String> results = new ArrayList<String>();
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();

            if (values != null) {

                for (List row : values) {
                    results.add("Унівеситет: "+row.get(0) +", Імя користувача: "+row.get(1)+ ", Викладач: " + row.get(2) + ", Оцінки(" + row.get(3)+ ", "+ row.get(4)+ ", "+
                            row.get(5)+ ", "+ row.get(6)+ ", "+ row.get(7)+ "),  дата і час: "+ row.get(8)+ ", коментар: "+ row.get(9)+"\n");
                }
            }
            Log.d(LOG_TAG, "Read from sheet");

            return results;
        }



        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                mOutputText.setText("No results returned.");
            } else {
                mOutputText.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            SheetsGoogle.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }

    private class MakeWriteRequestTask extends AsyncTask<OneReview, Void, Void> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        public MakeWriteRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("MyTestAPI")
                    .build();

        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected Void doInBackground(OneReview... params) {
            try {
                 setDataToApi(params[0]);
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
            }
            return null;
        }



        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         * @throws IOException
         * @param oneReview
         */
        private void setDataToApi(OneReview oneReview) throws IOException {
            String spreadsheetId = "1J03H-yudUHAA2gGvgYOAPvQNzwRNTn2faYjaqHo-Dc4";
            String range = "Sheet!A1:J1";
            List<List<Object>> myvalue = new ArrayList<>();
            List<Object> obj = new ArrayList<>();

            obj.add(0,oneReview.getUniversity());
            obj.add(1,oneReview.getUserName());
            obj.add(2,oneReview.getTeacherName());
            obj.add(3,oneReview.getRating1());
            obj.add(4,oneReview.getRating2());
            obj.add(5,oneReview.getRating3());
            obj.add(6,oneReview.getRating4());
            obj.add(7,oneReview.getRating5());
            obj.add(8,oneReview.getCurrentTime());
            obj.add(9,oneReview.getComment());


            myvalue.add(obj);


            ValueRange valueRange = new ValueRange();
            valueRange.setRange(range);

            valueRange.setValues(myvalue);

            mService.spreadsheets().values().append(spreadsheetId, range,valueRange).setValueInputOption("RAW").execute();

            Log.d(LOG_TAG, "Write in sheet");

        }



        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            SheetsGoogle.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }


    private class OneReview {


        public String university;
        public String userName;
        public String teacherName;

        public float rating1;
        public float rating2;
        public float rating3;
        public float rating4;
        public float rating5;

        public String comment;
        public  String currentTime;

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


        public OneReview (String university, String userName, String teacherName, float rating1, float rating2, float rating3, float rating4, float rating5, String comment){
            this.university = university;
            this.userName = userName;
            this.teacherName = teacherName;
            this.rating1 = rating1;
            this.rating2 = rating2;
            this.rating3 = rating3;
            this.rating4 = rating4;
            this.rating5 = rating5;
            this.comment = comment;

            this.currentTime = sdf.format(new Date());

        }


        public String getUserName() {
            return userName;
        }

        public String getUniversity() {
            return university;
        }


        public String getTeacherName() {
            return teacherName;
        }


        public float getRating1() {
            return rating1;
        }

        public float getRating2() {
            return rating2;
        }

        public float getRating3() {
            return rating3;
        }

        public float getRating4() {
            return rating4;
        }

        public float getRating5() {
            return rating5;
        }

        public String getComment() {
            return comment;
        }
        public String getCurrentTime() {
            return currentTime;
        }

    }
}

