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
public class FakeQuadEncoderTest extends TestClass implements TestHarness
{

    private final int MAXCOUNT = 500;
    private Counter counter;
    private Counter counter2;
    private FakeQuadEncoder fakeQEncoder;

    public String getName()
    {
        return "FakeQuadEncoder Test";
    }

    public void setup()
    {
        counter = new Counter(CROSSCONNECTAP1);
        counter2 = new Counter(CROSSCONNECTBP1);
        fakeQEncoder = new FakeQuadEncoder(CROSSCONNECTAP2, CROSSCONNECTBP2);
    }

    public void teardown()
    {
        counter.free();
        counter2.free();
        fakeQEncoder.free();
    }


    {
        new Test("Forward counting")
        {

            public void run()
            {
                counter.reset();
                counter2.reset();

                fakeQEncoder.setCount(MAXCOUNT);
                fakeQEncoder.setRate(1);
                counter.start();
                counter2.start();
                fakeQEncoder.execute();
                assertEquals(counter.get(), MAXCOUNT);
                assertEquals(counter2.get(), MAXCOUNT);
            }
        };

        new Test("Reverse counting")
        {

            public void run()
            {
                counter.reset();
                counter2.reset();
                fakeQEncoder.setForward(false);
                fakeQEncoder.setCount(MAXCOUNT);
                fakeQEncoder.setRate(1);
                counter.start();
                counter2.start();
                fakeQEncoder.execute();

                assertEquals(counter.get(), MAXCOUNT);
                assertEquals(counter2.get(), MAXCOUNT);

                counter.stop();
                counter2.stop();
            }
        };
    }
}
