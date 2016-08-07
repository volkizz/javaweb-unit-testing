package com.nago.overboard.model;

import com.nago.overboard.exc.AnswerAcceptanceException;
import com.nago.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private Board board;
    private User userAsking;
    private User userUnswerving;
    private Question question;
    private Answer answer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        board = new Board("Test");
        userAsking = board.createUser("Johnny");
        userUnswerving = board.createUser("Tommy");
        question = userAsking.askQuestion("Test question");
        answer = userUnswerving.answerQuestion(question, "Test answer");
    }

    @Test
    public void questionerReputationGoesUpBy5PointsIfTheirQuestionIsUpvoted() throws Exception {
        userUnswerving.upVote(question);

        assertEquals(5, userAsking.getReputation());
    }

    @Test
    public void answererReputationGoesUpBy10IfTheirAnswerIsUpvoted() throws Exception {
        userAsking.upVote(answer);

        assertEquals(10, userUnswerving.getReputation());
    }

    @Test
    public void answererReputationGoesDownBy1IfTheirAnswerIsDownvoted() throws Exception {
        userAsking.downVote(answer);

        assertEquals(-1, userUnswerving.getReputation());
    }

    @Test
    public void downVotingOfQuestionsAffectsNothing() throws Exception {
        userUnswerving.downVote(question);

        assertEquals(0, userAsking.getReputation());
    }

    @Test
    public void acceptedAnswerGivesTheAnswerer15PointReputationBoost() throws Exception {
        userAsking.acceptAnswer(answer);

        assertEquals(15, userUnswerving.getReputation());
    }

    @Test(expected = VotingException.class)
    public void votingUpIsNotAllowedOnQuestionsByTheOriginalAuthor() throws Exception {
        userAsking.upVote(question);
    }

    @Test(expected = VotingException.class)
    public void votingDownIsNotAllowedOnQuestionsByTheOriginalAuthor() throws Exception {
        userAsking.downVote(question);
    }

    @Test(expected = VotingException.class)
    public void votingUpIsNotAllowedOnAnswerByTheOriginalAuthor() throws Exception {
        userUnswerving.upVote(answer);
    }

    @Test(expected = VotingException.class)
    public void votingDownIsNotAllowedOnAnswerByTheOriginalAuthor() throws Exception {
        userUnswerving.upVote(answer);
    }

    @Test
    public void onlyTheOriginalQuestionerCanAcceptAnAnswer() {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage("Only Johnny can accept this answer as it is their question");

        userUnswerving.acceptAnswer(answer);
    }

    @Test
    public void QuestionIsAcceptedSuccessfully() throws Exception {
        userAsking.acceptAnswer(answer);

        assertEquals(true, answer.isAccepted());
    }
}