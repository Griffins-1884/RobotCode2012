/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import edu.wpi.first.testing.TestClass;

/**
 * @file FakeEncoderTest.java
 * Test the fake encoder classes to verify they are valid for future test use
 * @author Ryan O'Meara
 */
public class FakeEncoderTest extends TestClass implements TestHarness
{

    private Counter counter;
    private FakeEncoder fakeEncoder;

    public String getName()
    {
        return "FakeEncoder Test";
    }

    public void setup()
    {
        counter = new Counter(CROSSCONNECTAP2);
        fakeEncoder = new FakeEncoder(CROSSCONNECTAP1);
    }

    public void teardown()
    {
        counter.free();
        fakeEncoder.free();
    }


    {
        new Test("Counting accuracy")
        {

            public void run()
            {
                final int MAXCOUNT = 500;
                counter.reset();
                counter.start();

                fakeEncoder.setCount(MAXCOUNT);
                fakeEncoder.setRate(1);
                fakeEncoder.execute();
                assertEquals(counter.get(), MAXCOUNT);
                counter.stop();

            }
        };
    }
}
