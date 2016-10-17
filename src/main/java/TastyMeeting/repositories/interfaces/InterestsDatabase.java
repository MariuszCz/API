package TastyMeeting.repositories.interfaces;

import TastyMeeting.Exceptions.WrongIdException;
import TastyMeeting.data.Interest;

import java.util.List;

/**
 * Created by mariusz on 03.05.16.
 */
public interface InterestsDatabase {
    List<Interest> getInterests();

    void addInterest(Interest interest);

    void updateInterest(String id, Interest interest);

    void deleteInterest(String id) throws WrongIdException;

    Interest getInterest(String id);

    boolean checkIfInterestExists(String interestName);
}
