package com.restaurant.bdd;

import com.restaurant.bdd.steps.FoodDeliverySteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnit4Story;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;

@RunWith(JUnit4Story.class)
@Tag("bdd")
public class FoodDeliveryStory extends JUnit4Story {

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(this.getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withFormats(Format.CONSOLE, Format.HTML, Format.XML)
                        .withRelativeDirectory("target/jbehave-reports"));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new FoodDeliverySteps());
    }
}
