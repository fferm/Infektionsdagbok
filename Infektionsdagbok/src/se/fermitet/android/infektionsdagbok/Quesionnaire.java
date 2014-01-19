package se.fermitet.android.infektionsdagbok;

import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class Quesionnaire extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quesionnaire);
        setupViews();
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quesionnaire, menu);
        return true;
    }

	private void setupViews() {
		QuestionView generallyWell = (QuestionView) findViewById(R.id.generallyWell);
		generallyWell.setChecked(true);
	}


}
