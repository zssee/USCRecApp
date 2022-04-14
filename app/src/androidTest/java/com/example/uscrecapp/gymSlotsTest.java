package com.example.uscrecapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RunWith(AndroidJUnit4.class)
public class gymSlotsTest {

    @Rule
    public ActivityTestRule<gymSlots> mActivityTestRule = new ActivityTestRule<gymSlots>(gymSlots.class);
    public ActivityTestRule<SummaryPage> mActivityTestRule2 = new ActivityTestRule<SummaryPage>(SummaryPage.class);

    private gymSlots mActivity = null;
    private SummaryPage mActivity2 = null;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() throws Exception{
        mActivity = mActivityTestRule.getActivity();
        mActivity.selectedDay = "Tuesday";
        mActivity.selectedGym = "Lyon";
        mActivity2 = mActivityTestRule2.getActivity();
        mActivity2.docName = "kellyMa";
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @Test
    public void gymTitleTest(){
        mActivity.selectedGym = "Lyon";
        mActivity.setGymTitle();
        View view = mActivity.findViewById(R.id.gymTitle);
        assertNotNull(view);
    }

    @Test
    public void capcityTextTest(){
        mActivity.changeCapacityText("lyonMon10-12");
        View cap8 = mActivity.findViewById(R.id.capText8);
        assertNotNull(cap8);
        View cap10 = mActivity.findViewById(R.id.capText10);
        assertNotNull(cap10);
        View cap12 = mActivity.findViewById(R.id.capText12);
        assertNotNull(cap12);
        View cap2 = mActivity.findViewById(R.id.capText2);
        assertNotNull(cap2);
        View cap4 = mActivity.findViewById(R.id.capText4);
        assertNotNull(cap4);
    }

    @Test
    public void buttonTest(){
        mActivity.setUpButtons();
        Button b8 = mActivity.findViewById(R.id.reserve8);
        Button r8 = mActivity.findViewById(R.id.remind8);
        assertNotNull(b8);
        assertNotNull(r8);

        Button b10 = mActivity.findViewById(R.id.reserve10);
        Button r10 = mActivity.findViewById(R.id.remind10);
        assertNotNull(b10);
        assertNotNull(r10);

        Button b12 = mActivity.findViewById(R.id.reserve12);
        Button r12 = mActivity.findViewById(R.id.remind12);
        assertNotNull(b12);
        assertNotNull(r12);

        Button b2 = mActivity.findViewById(R.id.reserve2);
        Button r2 = mActivity.findViewById(R.id.remind2);
        assertNotNull(b2);
        assertNotNull(r2);

        Button b4 = mActivity.findViewById(R.id.reserve4);
        Button r4 = mActivity.findViewById(R.id.remind4);
        assertNotNull(b4);
        assertNotNull(r4);
    }

    @Test
    public void addUsertoSlot8Test(){
        onView(withId(R.id.reserve8)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoSlot10Test() {
        onView(withId(R.id.reserve10)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoSlot12Test() {
        onView(withId(R.id.reserve12)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoSlot2Test() {
        onView(withId(R.id.reserve2)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoSlot4Test() {
        onView(withId(R.id.reserve4)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoWaitlist8Test(){
        onView(withId(R.id.remind8)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoWaitlist10Test() {
        onView(withId(R.id.remind10)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoWaitlist12Test() {
        onView(withId(R.id.remind12)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoWaitlist2Test() {
        onView(withId(R.id.remind2)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @Test
    public void addUsertoWaitlist4Test() {
        onView(withId(R.id.remind4)).perform(click());
        assertEquals("Added successfully!", out.toString());
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        System.setOut(originalOut);
        System.setErr(originalErr);
    }



}