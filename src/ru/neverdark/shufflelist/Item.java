/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.shufflelist;

public class Item {
    private boolean mIsChecked;
    private int mPosition;
    private long mRecordId;
    private String mText;

    public Item(String text) {
        mText = text;
        mIsChecked = false;
    }

    public int getPosition() {
        return mPosition;
    }

    public long getRecordId() {
        return mRecordId;
    }

    public String getText() {
        return mText;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setIsChecked(boolean mIsChecked) {
        this.mIsChecked = mIsChecked;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setRecordId(long mRecordId) {
        this.mRecordId = mRecordId;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

}
