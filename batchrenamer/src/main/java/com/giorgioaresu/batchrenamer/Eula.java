package com.giorgioaresu.batchrenamer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;

import java.util.Calendar;

public class Eula {
    AlertDialog dialog = null;

    private static final String KEY_ACCEPTED_EULA = "eula_accepted";
    /**
     * Returns true if has accepted eula.
     *
     * @param context
     */
    public static boolean hasAcceptedEula(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(KEY_ACCEPTED_EULA, false);
    }

    /**
     * Sets to true accepted eula.
     *
     * @param context
     */
    public static void setAcceptedEula(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(KEY_ACCEPTED_EULA, true).apply();
    }

    /**
     * Show End User License Agreement.
     *
     * @param accepted True IF user has already accepted license, which means it can be dismissed.
     *                 If the user hasn't accepted, then the EULA must be accepted or the program
     *                 exits.
     * @param activity Activity started from.
     */
    public void show(final boolean accepted, final Activity activity) {
        Spanned message = Html.fromHtml(String.format(activity.getString(R.string.eula_text), activity.getString(R.string.app_name), Calendar.getInstance().get(Calendar.YEAR)));

        AlertDialog.Builder eula = new AlertDialog.Builder(activity)
                .setTitle(R.string.about_eula_dialog_title)
                .setMessage(message)
                .setCancelable(accepted);

        if (accepted) {
            // If they've accepted the EULA allow, show an OK to dismiss.
            eula.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            // If they haven't accepted the EULA allow, show accept/decline buttons and exit on
            // decline.
            eula
                    .setPositiveButton(R.string.accept,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setAcceptedEula(activity);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton(R.string.decline,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    activity.finish();
                                }
                            });
        }
        dialog = eula.create();
        dialog.show();
    }

    /**
     * Dismisses eula dialog
     */
    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
