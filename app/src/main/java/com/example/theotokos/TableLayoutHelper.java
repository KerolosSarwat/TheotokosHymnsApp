package com.example.theotokos;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;

public class TableLayoutHelper {

    public static TableLayout createTableLayout(Context context, String[] column1, String[] column2, String[] column3) {
        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        int rowCount = Math.max(column1.length, Math.max(column2.length, column3.length));

        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            // Handle empty values if array sizes differ
            String value1 = i < column1.length ? column1[i] : "";
            String value2 = i < column2.length ? column2[i] : "";
            String value3 = i < column3.length ? column3[i] : "";

            int fontSize = SettingsFragment.getFontSize();

            TextView textView1 = new TextView(context);
            textView1.setText(value1);
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            textView1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            textView1.setPadding(16, 8, 16, 10);
            tableRow.addView(textView1);

            TextView textView2 = new TextView(context);

            textView2.setText(value2);
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            Typeface typeface = ResourcesCompat.getFont(context, R.font.coptic_font);
            textView2.setTypeface(typeface);
            textView2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            textView2.setPadding(16, 8, 16, 10);
            tableRow.addView(textView2);

            TextView textView3 = new TextView(context);
            textView3.setText(value3);
            textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            textView3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            textView3.setPadding(16, 8, 16, 10);
            tableRow.addView(textView3);

            tableLayout.addView(tableRow);
        }

        return tableLayout;
    }
}