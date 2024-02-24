%%begin-meta%%
title=JUnit tips
author=Corey Beres
timestamp=2015-11-30T10:00:00Z
%%end-meta%%

![JUnit tests](/img/junit_tests.png "All of your unit tests should pass.")

JUnit can be an extremely helpful tool for testing your code. Any tests for your code are better than no tests, but there might be some ways to get even more out of your tests. Are you really sure that you have the lines and conditions covered that you think you have covered? When a test fails, how long does it take for you to find out the source of the failure?

I present some helpful tips for improving your JUnit tests. I've had the luck of working with some great engineers who really knew their way around JUnit, and I'll share some tips I learned from them.

%%read-more%%

First, two general tips that you'll see in the following examples:

0. Keep your tests small, and test a specific function in each test.
0. Give your test functions a descriptive name that makes it clear what's being tested.

### Fail for the right reasons

If a test is designed to fail, make sure it fails for the right reason. Have a similar test that passes that is setup exactly the same (minus the change that makes it fail). Otherwise, it's possible that a test has failed for a completely different reason. Then you aren't even testing the code you think you're testing.

```java
private static ObjToTest createValidObjToTest() {
    // create an instance of the class you want to test
}

@Test
public void testGetSomething() {
    final ObjToTest objToTest = createValidObjToTest();
    assertEquals("should return abc", "abc", objToTest.getSomething());
}

@Test(expected = IllegalArgumentException.class)
public void testGetSomething_InvalidName() {
    final ObjToTest objToTest = createValidObjToTest();
    objToTest.name = null;
    // should fail
    objToTest.getSomething();
}
```

### Append the actual value to the failure message

If an assertion will not give enough detail when it fails, add a message. If possible include the `String.valueOf(actual)` and any other helpful info. This is helpful wuth assertions like assertTrue, assertFalse, and assertNull. With these assertions, you don't get much more than an AssertionError.

```java
@Test
public void testGetSomething() {
    final ObjToTest objToTest = createValidObjToTest();
    final String actual = objToTest.getSomething();
    assertTrue("should start with \"abc\", but is " + actual, objToTest.startsWith("abc"));
}
```

### Use Hamcrest's matchers to extend JUnit

The Hamcrest project has created a library of matchers that can be used with JUnit's [assertThat](http://junit.org/apidocs/org/junit/Assert.html#assertThat%28java.lang.String,%20T,%20org.hamcrest.Matcher%29) function. You can read Hamcrest's official [tutorial](https://code.google.com/p/hamcrest/wiki/Tutorial) for more information.

Hamcrest offers more specific and more flexible matchers than the assertions offered by JUnit. You can even combine matchers, or write your own! For instance, this example shows how you can check that a number if greater than zero, but less than ten.

```java
@Test
public void testSize() {
    final ObjToTest objToTest = createValidObjToTest();
    final int actual = objToTest.size();
    assertThat("size", actual, allOf(greaterThan(0), lessThan(10)));
}
```

### Make tests as fast as possible

Make tests as fast as possible. No sleeping. If necessary, use a mock. Think about how often your tests will (should) be run. You can save a lot of time by making a slow test faster. You'll also be more likely to run the tests.

Say you have a class checks for a file every 10 seconds. Don't let your test run for 15 seconds. If you can't specify a shorter period (on the order or milliseconds), then use a mock. For instance, if you use `System.currentTimeMillis`, then use Joda-Time's [DateTimeUtils.currentTimeMillis()](http://www.joda.org/joda-time/apidocs/index.html) instead. Joda-Time provides an easy way to override this function for testing.


```java
@Test
public void testIsFileDetected() {
    final ObjToTest objToTest = createValidObjToTest();
    DateTimeUtils.setCurrentMillisFixed(10);
    assertTrue("isFileDetected", objToTest.isFileDetected());
}

@After
public void tearDown() {
    // reset to use System time after test
    DateTimeUtils.setCurrentMillisSystem();
}
```

### Don't rely on external dependencies

If a test might fail, you want to limit the possible reasons it could have failed. You also want fast tests. Using external dependencies in your tests like external web services and databases defeats these two requirements.

If you have an external requirement, use a mock. The dependency might provide a mock, like Kafka's [MockProducer](http://kafka.apache.org/082/javadoc/org/apache/kafka/clients/producer/MockProducer.html) or Android's [MockContext](http://developer.android.com/reference/android/test/mock/MockContext.html). If not, try [Mockito](http://mockito.org/). Here's a quick example.

```java
private ExternalWebService service;

@Before
public void setup() {
    service = mock(ExternalWebService.class);
}

private static ObjToTest createValidObjToTest() {
    // create an instance of the class you want to test
    ObjToTest objToTest = new ObjToTest();
    objToTest.setWebService(service);
    return objToTest;
}

@Test
public void testAddCategory() {
    final ObjToTest objToTest = createValidObjToTest();
    // mock the service function
    when(service.put(10)).thenReturn(true);
    assertTrue("tried to add a new value", objToTest.addCategory(10));
}
```
