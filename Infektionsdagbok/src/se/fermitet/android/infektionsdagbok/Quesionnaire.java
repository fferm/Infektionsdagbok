package se.fermitet.android.infektionsdagbok;

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
/*		QuestionView view = (QuestionView) findViewById(R.id.aQuestionLast);
		
    	ViewGroup parentOfQuestions = (ViewGroup) findViewById(R.id.parentOfQuestions);
    	
    	QuestionView newView = new QuestionView(this);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	params.addRule(RelativeLayout.BELOW, R.id.aQuestionLast);
    	newView.setQuestion("en ny fråga");

    	parentOfQuestions.addView(newView, params);*/
    	
	}
	

}
