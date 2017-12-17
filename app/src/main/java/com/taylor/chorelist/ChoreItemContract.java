package com.taylor.chorelist;

import android.provider.BaseColumns;

final class ChoreItemContract {
    private ChoreItemContract() {}

    public static class ChoreItem implements BaseColumns {
        public static final String TABLE_NAME="chore_items";
        public static final String COLUMN_NAME_CHORE_NAME="chore_name";
        public static final String COLUMN_NAME_CHORE_INTERVAL="interval";
    }
}
