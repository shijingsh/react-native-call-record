package com.mg365.call_record;

import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.database.Cursor;
import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

public class CallRecordModule extends ReactContextBaseJavaModule {

    private Context context;
    private ReadableMap options;
    private int limit = -1;
    private Boolean isDistinct = false;

    public CallRecordModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "RNCallRecord";
    }

    private void setConfiguration(final ReadableMap options) {
        if(options==null){
            return;
        }
        isDistinct = options.hasKey("isDistinct") && options.getBoolean("isDistinct");
        limit = options.hasKey("limit") ? options.getInt("limit") : width;
        this.options = options;
    }

    @ReactMethod
    public void getAll(final ReadableMap options , Promise promise) {

        setConfiguration(options);

        Cursor cursor = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");

        WritableArray result = Arguments.createArray();

        if (cursor == null) {
            promise.resolve(result);
            return;
        }

        int count = 0;

        final int NUMBER_COLUMN_INDEX = cursor.getColumnIndex(Calls.NUMBER);
        final int TYPE_COLUMN_INDEX = cursor.getColumnIndex(Calls.TYPE);
        final int DATE_COLUMN_INDEX = cursor.getColumnIndex(Calls.DATE);
        final int DURATION_COLUMN_INDEX = cursor.getColumnIndex(Calls.DURATION);
        final int NAME_COLUMN_INDEX = cursor.getColumnIndex(Calls.CACHED_NAME);
        Map<String,String> recordMap = new HashMap<>();

        while (cursor.moveToNext() && (limit < 0 || count++ < limit ) ) {
            String phoneNumber = cursor.getString(NUMBER_COLUMN_INDEX);
            int duration = cursor.getInt(DURATION_COLUMN_INDEX);
            String name = cursor.getString(NAME_COLUMN_INDEX);

            String timestampStr = cursor.getString(DATE_COLUMN_INDEX);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = df.format(new Date(Long.valueOf(timestampStr)));

            String type = this.getCallType(cursor.getInt(TYPE_COLUMN_INDEX));
            // no repeat
            if(isDistinct && recordMap.get(phoneNumber)!=null){
                continue;
            }
            WritableMap map = Arguments.createMap();
            map.putString("phoneNumber", phoneNumber);
            map.putInt("duration", duration);
            map.putString("name", name);
            map.putString("dateTime", dateTime);
            map.putString("type", type);

            recordMap.put(phoneNumber,"");
            result.pushMap(map);
        }

        cursor.close();

        promise.resolve(result);
    }

    private String getCallType(int type) {
        switch (type) {
            case Calls.OUTGOING_TYPE:
                return "OUTGOING_TYPE";
            case Calls.INCOMING_TYPE:
                return "INCOMING_TYPE";
            case Calls.MISSED_TYPE:
                return "MISSED_TYPE";
            default:
                return type+"";
        }
    }
}

