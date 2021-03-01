package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabRequestController;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.lab.TestStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Slf4j
class LabRequestControllerTest {


    @Autowired
    LabRequestController labRequestController;

    @InjectMocks
    TestRequestUpdateService testRequestUpdateService;

    @Mock
    TestRequestUpdateService testRequestUpdateService;

    @Mock
    UserLoggedInService userLoggedInService;
    
    @Autowired
    TestRequestQueryService testRequestQueryService;


    @Test
    @WithUserDetails(value = "tester")
    public void calling_assignForLabTest_with_valid_test_request_id_should_update_the_request_status(){

        //Arrange
        CreateLabResult tester = getCreateLabResult();
        TestRequest testRequest = getTestRequestByStatus(RequestStatus.INITIATED);
        LabRequestController labRequestController = new LabRequestController();
        
        
        //Create another object of the TestRequest method and explicitly assign this object for Lab Test using assignForLabTest() method
        // from labRequestController class. Pass the request id of testRequest object.
        
        //Act
        TestRequest anotherTestRequest = testRequest.assignForLabTest();
        Mockito.when(labRequestController.id).thenReturn(anotherTestRequest);

        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'INITIATED'
        // make use of assertNotNull() method to make sure that the lab result of second object is not null
        // use getLabResult() method to get the lab result
        
        //Assert
        assertEquals(testRequest.id, anotherTestRequest.id);
        assertEquals(anotherTestRequest.status, "INITIATED");
        assertNotNull(anotherTestRequest.getLabResult());
    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        return testRequestQueryService.findBy(status).stream().findFirst().get();
    }

    @Test
    @WithUserDetails(value = "tester")
    @ExtendWith(MockitoExyension.class)
    public void calling_assignForLabTest_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Assent
        ResponseStatusException responseStatusException = new ResponseStatusException();

        User tester = creatUser();

        when(responseStatusException.assertThrows()).thenReturn(testRequestUpdateService.assignForLabTest(id,tester));
        //Act

        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForLabTest() method
        // of labRequestController with InvalidRequestId as Id
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            testRequestController.assignForLabTest(createTestRequest);
        });
        //accent
        
        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        testRequestUpdateService.assignForLabTest().isEqualTo(11);
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_valid_test_request_id_should_update_the_request_status_and_update_test_request_details(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method
        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        CreateLabResult createLabResult = getCreateLabResult(testRequest);

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'LAB_TEST_IN_PROGRESS'. Make use of updateLabTest() method from labRequestController class (Pass the previously created two objects as parameters)
        TestRequest anotherTestRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);
        LabRequestController labRequestController = new LabRequestController();
        labRequestController.updateLabTest(testRequest,anotherTestRequest);

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'LAB_TEST_COMPLETED'
        // 3. the results of both the objects created should be same. Make use of getLabResult() method to get the results.

        assertEquals(testRequest.id, anotherTestRequest.id);
        assertEquals(anotherTestRequest.status, "LAB_TEST_COMPLETED");
        assertEquals(testRequest.getLabResult(), anotherTestRequest.getLabResult());
    }


    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_test_request_id_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);


        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        CreateLabResult createLabResult = getCreateLabResult(testRequest);

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            testRequestController.createRequest(createTestRequest);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertEquals("Invalid ID" , result.getReason());

    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_empty_status_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        // Set the result of the above created object to null.

        CreateLabResult createLabResult = new CreateLabResult();
        createLabResult.set(null);

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            testRequestController.createRequest(createTestRequest);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "ConstraintViolationException"
        assertEquals("ConstraintViolationException",result.getReason());


    }

    public CreateLabResult getCreateLabResult(TestRequest testRequest) {

        //Create an object of CreateLabResult and set all the values
        // Return the object
        CreateLabResult createLabResult = new CreateLabResult();
        createLabResult.setUserName('test');
        createLabResult.setId(12R);
        createLabResult.setStatus('Positive');
        createLabResult.setNumber('321452');
        createLabResult.setDetail('add');
        createLabResult.setPriority('high');

        return createLabResult; // Replace this line with your code
    }

}