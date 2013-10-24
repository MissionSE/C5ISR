package com.missionse.databaseexample;

import java.util.List;
import java.util.Random;

import org.orman.mapper.Model;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.databaseexample.R;
import com.missionse.databaseexample.Model.Student;


public class CreateNew extends ListFragment {
	
	private Button mButtonStudent;
	private Button mButtonClassRoom;
	private ListView mListView;
	private List<Student> students;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View lRootView = inflater.inflate(R.layout.database_dummy, container, false);
		
		
		initComponents(lRootView);
		
		updateStudentList();
		
		
		return lRootView;
		
	}
	
	
	private void initComponents(View tRootView){
		
		initButton(tRootView);
		
	}
	
	private void initButton(View tRootView){
		mButtonStudent = (Button)tRootView.findViewById(R.id.section_button_one);
		mButtonClassRoom = (Button)tRootView.findViewById(R.id.section_button_two);
		
		mButtonStudent.setText("AddStudent");
		mButtonClassRoom.setText("AddClassRoom");
		
		mButtonStudent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				createNewStudent(arg0);
			}
			
		});
		
		mButtonClassRoom.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View tView){
				createNewClassRoom(tView);
			}
		});
	
		
	}
	
	private void createNewStudent(View tView){
		Random r = new Random();
		Student s = new Student();
		s.mFirstName = "RV" + r.nextInt();
		s.mLastName = "BV" + r.nextInt();
		s.insert();
		
		updateStudentList();
		
	}
	
	private void createNewClassRoom(View tView){
		
	}
	
	
	private void updateStudentList(){
		students = Model.fetchAll(Student.class);
		
		ListAdapter l = 
				new ArrayAdapter<Student>(getActivity().getApplicationContext(),R.layout.student_list_textview,
						students.toArray(new Student[students.size()]));
		setListAdapter(l);
	}
	
	

}
