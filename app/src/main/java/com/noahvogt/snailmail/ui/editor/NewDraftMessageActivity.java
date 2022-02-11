/* Note: this file is derived from a template located in the following repository:
 * https://github.com/googlecodelabs/android-room-with-a-view
 * 
 * This means was originally licensed under the Apache License 2.0, which can be found
 * in the text file 'APL' in the root directory of this repository. Any changes made to
 * this file however are licensed under the GNU General Public License Version 3.0,
 * which can be found in the file 'LICENSE' in the root directory of this repository.
 */

package com.noahvogt.snailmail.ui.editor;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.noahvogt.snailmail.R;

public class NewDraftMessageActivity extends AppCompatActivity {

   @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mailbox_inbox_fragment);

       NewDraftMessageActivity.this.finish();
        }
    }
