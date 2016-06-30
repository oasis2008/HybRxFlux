/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.yuntongxun.kitsdk.ui;

import android.content.Intent;


import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;





import java.io.File;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.kitsdk.adapter.FileListAdapter;
import com.yuntongxun.kitsdk.utils.FileUtils;


public class ECFileExplorerActivity extends ECSuperActivity implements
		View.OnClickListener {

	/**设备内置目录*/
	private static final int DIR_ROOT = 0;
	/**外置存储卡*/
	private static final int DIR_SDCARD = 1;
	/**文件浏览器列表*/
	private ListView mFileListView;
	/**设备选项卡*/
	private TextView mRootTab;
	/**设备选项卡导航线*/
	private View mRootTabSelector;
	/**存储卡选项卡*/
	private TextView mSdcardTab;
	/**存储卡导航线*/
	private View mSdcardTabSelector;
	
	private String mFileExplorerRootTag;
	private String mFileExplorerSdcardTag;
	private FileListAdapter mAdapter;
	private File mRootFile;
	private File mSdcardFile;
	/**浏览文件目录类型*/
	private int mType = DIR_ROOT;
	
	final private AdapterView.OnItemClickListener mItemClickListener
		= new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				File file = (File) mAdapter.getItem(position);
				if(!file.isFile()) {
					if(mType == DIR_ROOT) {
						// 当前为内置存储浏览模式
						mRootFile = file;
					} else {
						mSdcardFile = file;
					}
					
					if(file != mAdapter.getParentFile()) {
						mAdapter.setFiles(mAdapter.getCurrentFile(), file);
					} else {
						mAdapter.setFiles(mAdapter.getParentFile().getParentFile(), mAdapter.getParentFile());
					}
					
					mAdapter.notifyDataSetChanged();
					mFileListView.setSelection(0);
					return ;
				}

			setResult(RESULT_OK, new Intent().putExtra("choosed_file_path", file.getAbsolutePath()));
			finish();
			}
		};
		
	@Override
	protected int getLayoutId() {
		return R.layout.ytx_file_explorer;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String title = getIntent().getStringExtra("key_title");
		if(TextUtils.isEmpty(title)) {
			getTopBarView().setTopBarToStatus(1, R.drawable.ytx_topbar_back_bt, -1, R.string.plugin_file_explorer_ui_title, this);
		} else {
			getTopBarView().setTopBarToStatus(1, R.drawable.ytx_topbar_back_bt, -1, title, this);
		}
		
		initFileExplorer();
	}

	/**
	 * 初始化页面
	 */
	private void initFileExplorer() {
		mFileListView = (ListView) findViewById(R.id.file_explorer_list_lv);
		
		

		
		File externalStorageFile = null;
		if(!FileUtils.checkExternalStorageCanWrite()) {
			File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
			if(downloadCacheDirectory.canRead()) {
				externalStorageFile = downloadCacheDirectory;
				mFileExplorerSdcardTag = downloadCacheDirectory.getName();
			}
		} else {
			externalStorageFile = Environment.getExternalStorageDirectory();
			
		}
		mSdcardFile = externalStorageFile;
		


		mAdapter = new FileListAdapter(this);
		mAdapter.setPath(externalStorageFile.getPath());
		mAdapter.setFiles(externalStorageFile.getParentFile(), externalStorageFile);
		mFileListView.setAdapter(mAdapter);
		mFileListView.setOnItemClickListener(mItemClickListener);
	}
	
	

	@Override
	public void onClick(View v) {
		
		if(v.getId()==R.id.btn_left){
			hideSoftKeyboard();
			finish();
			
		}
		
	}
	
	private class FileTabClickListener implements View.OnClickListener {

		private int type;
		/**当前浏览文件夹*/
		private File mParentFile;
		/**扩展卡根目录*/
		private File mRootPath;
		public FileTabClickListener(int type , File f) {
			this.type = type;
			this.mRootPath = f;
		}
		
		@Override
		public void onClick(View v) {
			mParentFile = (this.type == DIR_SDCARD) ? mSdcardFile : mRootFile;
			mAdapter.setPath(mRootPath.getPath());
			mAdapter.setFiles(mParentFile.getParentFile(), mParentFile);
			mAdapter.notifyDataSetInvalidated();
			mAdapter.notifyDataSetChanged();
			mFileListView.setSelection(0);
		}
		
	}
}
