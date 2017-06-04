package com.mooo.sestus.indoor_locator.selectfloorplan;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.addfloorplan.AddFloorPlanActivity;
import com.mooo.sestus.indoor_locator.data.FakeFloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.viewfloorplan.ViewFloorPlanActivity;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.SortedSet;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class SelectFloorPlanActivityTest {

    @Rule
    public final IntentsTestRule<SelectFloorPlanActivity> activityTestRule = new IntentsTestRule<>(SelectFloorPlanActivity.class, false, false);
    private Matcher<View> selectFloorPlanButton;
    private Matcher<View> addNewFloorPlanButton;

    @Before
    public void setUp() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), SelectFloorPlanActivity.class);
        activityTestRule.launchActivity(intent);
        selectFloorPlanButton = withId(R.id.btn_select_floor_plan);
        addNewFloorPlanButton = withId(R.id.btn_add_floor_plan);
    }

    @Test
    public void AddFloorPlanButton_OnClick_StartsAddFloorPlanActivity() {
        onView(addNewFloorPlanButton).perform(click());

        intended(hasComponent(AddFloorPlanActivity.class.getName()));
    }


    @Test
    public void SpinnerItem_OnClick_StartsViewFloorPlanActivity() {
        SortedSet<FloorPlan> floorPlanSet = FakeFloorPlanRepository.getInstance().getFloorPlans();

        onView(selectFloorPlanButton).perform(click());

        intended(allOf(hasComponent(ViewFloorPlanActivity.class.getName()),
                hasExtra(ViewFloorPlanActivity.FLOOR_PLAN_ID, floorPlanSet.first().getId())));
    }


}