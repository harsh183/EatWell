package com.aditya.filebrowser.fileoperations;

import android.content.Context;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.models.FileItem;
import com.aditya.filebrowser.utils.UIUtils;

import java.util.List;

/**
 * Created by adik on 9/27/2015.
 */
public class Operations {

    public enum FILE_OPERATIONS {
        CUT,
        COPY,
        NONE
    }

    private List<FileItem> selectedFiles;

    public void resetOperation() {
        if(selectedFiles!=null)
            selectedFiles.clear();
        selectedFiles  = null;
        setOperation(FILE_OPERATIONS.NONE);
    }

    public FILE_OPERATIONS getOperation() {
        return currOperation;
    }

    public void setOperation(FILE_OPERATIONS operationn) {
        this.currOperation = operationn;
    }

    private FILE_OPERATIONS currOperation;

    public List<FileItem> getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles(List<FileItem> selectedItems) {
        this.selectedFiles = selectedItems;
        UIUtils.ShowToast("Selected "+selectedItems.size()+" items",mContext);
    }

    private static Operations op;

    private Context mContext;

    private Operations(Context mContext)
    {
        this.mContext = mContext;
        this.currOperation = FILE_OPERATIONS.NONE;
    }

    public static Operations getInstance(Context mContext)
    {
        if(op==null)
        {
            op = new Operations(mContext);
        }
        return op;
    }

    public Constants.SORT_OPTIONS getmCurrentSortOption() {
        return mCurrentSortOption;
    }

    public void setmCurrentSortOption(Constants.SORT_OPTIONS mCurrentSortOption) {
        this.mCurrentSortOption = mCurrentSortOption;
    }

    public Constants.FILTER_OPTIONS getmCurrentFilterOption() {
        return mCurrentFilterOption;
    }

    public void setmCurrentFilterOption(Constants.FILTER_OPTIONS mCurrentFilterOption) {
        this.mCurrentFilterOption = mCurrentFilterOption;
    }

    private Constants.SORT_OPTIONS mCurrentSortOption = Constants.SORT_OPTIONS.NAME;

    private Constants.FILTER_OPTIONS mCurrentFilterOption = Constants.FILTER_OPTIONS.ALL;
}
