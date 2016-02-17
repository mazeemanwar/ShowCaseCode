package com.tracker.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Utility functions to deal with general things.
 *
 * @author Muhammad Azeem Anwar
 */

public class Utilities {
    private static final Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
    private static final String _ID = ContactsContract.Contacts._ID;
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

    private static final Uri EMAIL_CONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private static final String EMAIL_CONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
    private static final String EMAIL = ContactsContract.CommonDataKinds.Email.DATA;

    /**
     * Checks if data connection is enabled.
     *
     * @return true if it's enabled
     */
    public static boolean checkDataConnection(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // Check if device is a mobile phone
        if (isMobilePhone(context)) {
            // Check if wifi or mobile network is available. Returns true if it
            // is,
            // otherwise it will return false
            NetworkInfo mobile = connec
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return wifi.isConnected() || mobile.isConnected();
        }

        // If it's not a mobile phone then check only wifi
        return wifi.isConnected();
    }

    /**
     * Checks if GPS is enabled
     *
     * @param context
     * @return
     */
    public static boolean checkGPSEnabled(Context context) {
        final LocationManager manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Checks if network is enabled
     *
     * @param context
     * @return
     */
    public static boolean checkNetworkEnabled(Context context) {
        final LocationManager manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Checks if device is a mobile phone.
     *
     * @return
     */
    public static boolean isMobilePhone(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM)
            return true;

        return false;
    }

    /**
     * Converts date string from given format to given format with universal
     * time format
     *
     * @param fromFormat - i.e "dd-MM-yyyy"
     * @param toFormat   - i.e "yyyy-MM-dd"
     * @return
     */
    public static String convertDateFormatUni(String date, String fromFormat,
                                              String toFormat) {
        SimpleDateFormat format = new SimpleDateFormat(fromFormat,
                Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();

        try {
            Date d = format.parse(date);
            calendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateFormat.format(toFormat, calendar.getTime()).toString();
    }

    /**
     * Converts date string from given format to given format.
     *
     * @param fromFormat - i.e "dd-MM-yyyy"
     * @param toFormat   - i.e "yyyy-MM-dd"
     * @return
     */
    public static String convertDateFormat(String date, String fromFormat,
                                           String toFormat) {
        SimpleDateFormat format = new SimpleDateFormat(fromFormat);
        Calendar calendar = Calendar.getInstance();

        try {
            Date d = format.parse(date);
            calendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateFormat.format(toFormat, calendar.getTime()).toString();
    }
     /* Converts String to date Stirng According to BAAS Box Server.
     *
	 * @param dateText
	 *            string date
	 * @return
	 */

    public static String convertDateToBAServerFormat(String dateText) {
        String str = dateText.replace("-", "/");
        String formatedDate = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date d = format.parse(str);
            SimpleDateFormat serverFormat = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ssZ");
            formatedDate = serverFormat.format(d);
            System.out.println(formatedDate);
            return serverFormat.format(d);
        } catch (ParseException e) {
            System.out.println("printing date Exception ==> " + e.toString());
            e.printStackTrace();

        }
        return formatedDate;
    }

    /**
     * Converts date from String.
     *
     * @param date - i.e "dd-MM-yyyy"
     * @param date - i.e "yyyy-MM-dd"
     * @return
     */

    public static Date convertDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
            Date date1 = dateFormat.parse(date);
            // SimpleDateFormat serverFormat = new
            // SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            System.out.println(date1);
            return date1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts date from String.
     *
     * @param date - i.e "dd-MM-yyyy"
     * @param date - i.e "yyyy-MM-dd"
     * @return
     */

    public static String convertDCDateFormat(String date) {
        String str = date.replace("-", "/");
        String formatedDate = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date d = format.parse(str);
            SimpleDateFormat serverFormat = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ssZ");
            formatedDate = serverFormat.format(d);
            System.out.println(formatedDate);
            return serverFormat.format(d);
        } catch (ParseException e) {
            System.out.println("printing date Exception ==> " + e.toString());
            e.printStackTrace();

        }
        return formatedDate;
    }

    /**
     * Extracts time from given date
     *
     * @param date
     * @return
     */
    public static long getTimeFromString(String date, String date2) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
            long Mins = 0;
            try {
                Date Date1 = format.parse(date);
                Date Date2 = format.parse(date2);
                long mills = Date2.getTime() - Date1.getTime();
                int Hours = (int) (mills / (1000 * 60 * 60));
                int s = (int) (mills / (1000 * 60));
                System.out.println(s);
                Mins = s;
                System.out.println(Mins);
            } catch (ParseException e) {

            }

            return Mins;
        }

        return 0;
    }

    /**
     * Converts date from String.
     *
     * @param date - i.e "dd-MM-yyyy"
     * @param date - i.e "yyyy-MM-dd"
     * @return
     */

    public static String changeDateFormat(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat("MMM dd, yyyy");
            formatedDate = serverFormat.format(d);
            return serverFormat.format(d);
        } catch (ParseException e) {
            System.out.println("printing date Exception ==> " + e.toString());
            e.printStackTrace();

        }
        return formatedDate;
    }

    public static Date convertDateFromGivenFormat(String date,
                                                  String fromFormat, String toFormat) {
        SimpleDateFormat format = new SimpleDateFormat(fromFormat);
        Date d = null;

        try {
            d = format.parse(date);
        } catch (ParseException e) {
            d = Calendar.getInstance().getTime();
            e.printStackTrace();
        }

        return d;

    }

    /**
     * Extracts time from given date
     *
     * @param date
     * @return
     */
    public static String getTimeFromDate(String date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm");
            Calendar calendar = Calendar.getInstance();

            try {
                Date d = dateFormat.parse(date);
                calendar.setTime(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        }

        return null;
    }

    /**
     * Formats number of minutes into string with format
     * "days : hours : minutes", e.g. "1 day 5 hours 10 minutes"
     *
     * @param minutes
     * @return formated string
     */
    public static String formatMinutesIntoDays(long minutes) {
        if (minutes == 0)
            return "0 Minutes";

        // Checks if there are enough minutes for an hour
        if (minutes >= 60) {
            long minutesLocal = minutes;
            int hours = (int) (minutesLocal / 60);
            minutesLocal -= hours * 60;

            StringBuilder builder = new StringBuilder();

            // Checks if there are enough hours for a day
            if (hours >= 24) {
                int days = hours / 24;
                hours -= days * 24;

                // Checks if there are any minutes, if so it will display hours
                // even if there are no hours
                if (minutesLocal > 0) {
                    builder.append(hours);

                    if (hours == 1)
                        builder.append(" Hour");
                    else
                        builder.append(" Hours");

                    builder.append(minutesLocal);

                    if (minutes == 1)
                        builder.append(" Minute");
                    else
                        builder.append(" Minutes");
                }
                // If there are no minutes...
                else {
                    // Checks if there are any hours, if they are then displays
                    // hours otherwise it doesn't display them
                    if (hours > 0) {
                        builder.append(hours);

                        if (hours == 1)
                            builder.append(" Hour");
                        else
                            builder.append(" Hours");
                    }
                }

                if (days == 1)
                    return "" + days + " Day " + builder.toString();
                else
                    return "" + days + " Days " + builder.toString();
            }
            // Otherwise there are no days, only hours and minutes
            else {
                if (minutes > 0) {
                    builder.append(minutesLocal);

                    if (minutes == 1)
                        builder.append(" Minute");
                    else
                        builder.append(" Minutes");
                }

                if (hours == 1)
                    return "" + hours + " Hour " + builder.toString();
                else
                    return "" + hours + " Hours " + builder.toString();
            }
        }
        // Otherwise there are no hours, only minutes
        else {
            if (minutes == 1)
                return "" + minutes + " Minute";
            else
                return "" + minutes + " Minutes";
        }
    }

    /**
     * Returns array of only emails from the address book
     *
     * @return
     */
    public static ArrayList<String> getEmailsFromAddressBook(Context context) {
        ArrayList<String> emails = new ArrayList<String>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
                null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contact_id = cursor
                        .getString(cursor.getColumnIndex(_ID));

                // Query and loop for every email of the contact
                Cursor emailCurosr = contentResolver.query(EMAIL_CONTENT_URI,
                        null, EMAIL_CONTACT_ID + "=?",
                        new String[]{contact_id}, null);

                while (emailCurosr.moveToNext()) {
                    String email = emailCurosr.getString(emailCurosr
                            .getColumnIndex(EMAIL));
                    email = email.toLowerCase();
                    emails.add(email);
                }
                emailCurosr.close();
            }

            cursor.close();
        }

        return emails;
    }

    /**
     * Returns array of contacts from address book. Contact consists of email
     * and full name.
     *
     * @return
     */
    public static ArrayList<ArrayList<String>> getContactsFromAddressBook(
            Context context) {
        ArrayList<ArrayList<String>> contacts = new ArrayList<ArrayList<String>>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
                null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contact_id = cursor
                        .getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor
                        .getColumnIndex(DISPLAY_NAME));

                // Query and loop for every email of the contact
                Cursor emailCurosr = contentResolver.query(EMAIL_CONTENT_URI,
                        null, EMAIL_CONTACT_ID + "=?",
                        new String[]{contact_id}, null);

                while (emailCurosr.moveToNext()) {
                    ArrayList<String> contact = new ArrayList<String>();

                    String email = emailCurosr.getString(emailCurosr
                            .getColumnIndex(EMAIL));
                    email = email.toLowerCase();

                    contact.add(name);
                    contact.add(email);
                    contacts.add(contact);
                }

                emailCurosr.close();
            }

            cursor.close();
        }

        return contacts;
    }

    /**
     * Sends email to given address with a given subject.
     *
     * @param context
     * @param emailTo   - recipient of the email
     * @param emailCC   - forward email to
     * @param subject   - subject of the email
     * @param emailText - body of the email
     * @param filePaths - paths to files to send
     */
    public static void sendEmail(Context context, String emailTo,
                                 String emailCC, String subject, String emailText,
                                 List<String> filePaths) {
        // need to "send multiple" to get more than one attachment
        final Intent emailIntent = new Intent(
                Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_CC,
                new String[]{emailCC});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
        // has to be an ArrayList
        ArrayList<Uri> uris = new ArrayList<Uri>();
        // convert from paths to Android friendly Parcelable Uri's
        for (String file : filePaths) {
            File fileIn = new File(file);
            fileIn.setReadable(true, false);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);

        }

        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    /**
     * Sends email to given address with a given subject.
     *
     * @param context
     * @param emailTo   - recipient of the email
     * @param emailCC   - forward email to
     * @param subject   - subject of the email
     * @param emailText - body of the email
     */
    public static void sendEmailWithotAttachment(Context context, String emailTo,
                                                 String emailCC, String subject, String emailText
    ) {
        // need to "send multiple" to get more than one attachment
        final Intent emailIntent = new Intent(
                Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_CC,
                new String[]{emailCC});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
        // has to be an ArrayList


        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    /**
     * Gets month from integer
     *
     * @param monthNumber
     * @return
     */
    public static String getMonthFromInt(int monthNumber) {
        String monthName = "";

        if (monthNumber >= 0 && monthNumber < 12)
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            } catch (Exception e) {
                if (e != null)
                    e.printStackTrace();
            }
        return monthName;
    }

    /**
     * Gets time from integer
     *
     * @param mInt
     * @return
     */
    public static String intToTime(int mInt) {
        String mTime;
        if (mInt <= 9) {
            mTime = "0" + mInt;
        } else
            mTime = String.valueOf(mInt);
        return mTime;
    }

    /**
     * Rounds a float number to two decimals
     *
     * @param f
     * @return
     */
    public static double roundTwoDecimals(double f) {
        double res = (double) (Math.round(f * 100)) / 100;
        return res;
    }

    /**
     * Hides soft keyboard
     *
     * @param v
     */
    public static void hideIM(Context context, View v) {
        try {
            InputMethodManager im = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = v.getWindowToken();

            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates duration between two dates.
     *
     * @param d1 - start date
     * @param t1 - start time
     * @param d2 - end date
     * @param t2 - end time
     * @return
     */
    public static long calculateDuration(String d1, String t1, String d2,
                                         String t2) {
        long duration = 0;

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            String datetime1 = d1 + " " + t1;
            String datetime2 = d2 + " " + t2;
            Date date1;
            date1 = sdf.parse(datetime1);
            Date date2 = sdf.parse(datetime2);
            duration = date2.getTime() - date1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return duration / (60 * 1000);

    }

    /**
     * Calculates duration between two times.
     *
     * @param t1 - end date
     * @param t2 - end time
     * @return
     */
    public static long calculateTimeDuration(String t1, String t2) {

        long diff = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String datetime1 = today + " " + t1;
            String datetime2 = today + " " + t2;
            Date date1;
            date1 = sdf.parse(datetime1);
            Date date2 = sdf.parse(datetime2);
            diff = date2.getTime() - date1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff / (60 * 1000) % 60;

    }

    /**
     * Gets the first number including decimal number it finds in the text.
     *
     * @param str - string to extract number from
     * @return extracted number
     */
    public static String getFirstNumberDecimalFromText(String str) {
        if (str == null) {
            return null;
        }

        StringBuffer strBuff = new StringBuffer();
        char c;
        boolean foundDecimal = false;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                strBuff.append(c);
            } else if (c == '.' && !foundDecimal) {
                strBuff.append(c);
                foundDecimal = true;
            }
        }
        return strBuff.toString();
    }

    /**
     * Gets the first number it finds in the text, excluding decimals.
     *
     * @param str - string to extract number from
     * @return extracted number
     */
    public static String getFirstNumberFromText(String str) {
        if (str == null) {
            return null;
        }

        StringBuffer strBuff = new StringBuffer();
        char c;
        boolean foundNumber = false;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                strBuff.append(c);
                foundNumber = true;
            }
            // Finds number first, then it will check if it's decimal
            else if (c == '.' && foundNumber)
                break;
        }
        return strBuff.toString();
    }

    /**
     * Gets set of paired Bluetooth devices.
     *
     * @return
     */
//    public static Set<BluetoothDevice> getBluetoothPairedDevices() {
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
//                .getDefaultAdapter();
//        return bluetoothAdapter.getBondedDevices();
//    }

    /**
     * @param context activtiy context
     * @return boolean haveNetworkConnection
     */
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean isValidDate(String dateString) {
        dateString = Utilities.convertDateFormat(dateString, "MMM dd, yyyy",
                "dd-MM-yyyy");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();
        Date date;
        try {
            date = format.parse(dateString);
            if (!date.before(today)) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println();

        return false;
    }

    public static float roundTwoDecimals(float f) {
        float res = (float) (Math.round(f * 100)) / 100;
        return res;
    }

    /**
     * Rounds a float number to two decimals
     *
     * @param f
     * @return
     */
    public static float roundTwoDecimalsDistanc(float f) {
        float res = (float) (Math.round(f * 100)) / 100;
        return res;
    }

    /**
     * ADD one month in the give date
     *
     * @param date
     * @return
     */

    public static Date addOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        System.out.println(cal.getTime());
        return cal.getTime();
    }

    public static Boolean locationInit(final Context context, final Activity a, final Activity abs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!Utilities.checkNetworkEnabled(context)
                && !Utilities.checkGPSEnabled(context)) {
            builder.setMessage("Please turn on Location Services in your System Settings.");
            builder.setTitle("Notification");
            builder.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
//							loading.setVisibility(View.GONE);
                        }
                    });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    context, abs.getClass());
//							loading.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                    });

            builder.setCancelable(false);
            builder.create().show();
            return false;
        }
        return true;
    }
    // Animation for all Activity
    public static void animateActivity( Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity. getWindow().setEnterTransition(new Explode());

            activity.   getWindow().setExitTransition(new Explode());

            activity. getWindow().setReturnTransition(new Slide());

        }
    }
}
