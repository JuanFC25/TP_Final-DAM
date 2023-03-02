package espresso;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;


import androidx.test.core.app.ActivityScenario;

import androidx.test.espresso.Root;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import com.malwarescanner.MainActivity;
import com.malwarescanner.R;


import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    private View decorView;
    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    public void archivoSubido(){
        onView(withId(R.id.nuevoArchivo)).check(matches(isDisplayed()));
        onView(withId(R.id.nuevoArchivo)).perform(click());
        onView(withText("¿Desea subir el archivo seleccionado?")).check(matches(isDisplayed()));
        onView(withText("Subir")).perform(click());
    }

    //no funciona porque no reconoce el Toast
  /*  @Test
    public void borrarArchivosListaVacia(){
        onView(withId(R.id.borrarArchivos)).check(matches(isDisplayed()));
        onView(withId(R.id.borrarArchivos)).perform(click());
        onView(withText("No hay archivos para borrar")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
*/
    @Test
    public void verFAQ(){
        onView(withId(R.id.menuAyuda)).check(matches(isDisplayed()));
        onView(withId(R.id.menuAyuda)).perform(click());
    }

    @Test
    public void verSettings(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
// Choose item "Settings"
        onView(withText("Opciones")).perform(click());
// Check that settings activity was opened
        onView(withText("Pedir permisos para mensaje")).check(matches(isDisplayed()));

    }

    @Test
    public void ListarMensajes(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
// Choose item "Settings"
        onView(withText("Anti SMS Phishing")).perform(click());
// Check that settings activity was opened
        onView(withId(R.id.nuevoSMS)).perform(click());
        onView(withText("Confirmación")).check(matches(isDisplayed()));
        onView(withText("Si")).perform(click());
    }

    @Test
    public void borrarArchivos(){
       //es necesario tener algun archivo subido
        onView(withId(R.id.borrarArchivos)).check(matches(isDisplayed()));
        onView(withId(R.id.borrarArchivos)).perform(click());
        onView(withText("Confirmación")).check(matches(isDisplayed()));
        onView(withText("Si")).perform(click());
    }
}


class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    @Override
    public boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
            IBinder windowToken = root.getDecorView().getWindowToken();
            IBinder appToken = root.getDecorView().getApplicationWindowToken();
            if (windowToken == appToken) {
                // windowToken == appToken means this window isn't contained by any other windows.
                // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                return true;
            }
        }
        return false;
    }

}