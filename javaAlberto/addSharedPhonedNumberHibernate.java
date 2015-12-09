package Sandbox.Alberto.Arellano;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HibernateRunner {
    private List<User> users;
    private HibernateConfig theHibernateUtility;

    public HibernateRunner(){
        theHibernateUtility = new HibernateConfig();
    }

    public static void main(String[] args){

        // Instance of hibernate
        HibernateRunner aSillyHibernateUseExample = new HibernateRunner();
        // Add NEW users to database
        // aSillyHibernateUseExample.addNewUsers();
        // Show users information
        // aSillyHibernateUseExample.showAllUsers();
        // Update user information
        // aSillyHibernateUseExample.modifyUser()
        // Add a number to one of the users
        aSillyHibernateUseExample.addSharedPhoneNumber();


        }


        private void addSharedPhoneNumber() {
            Session session = theHibernateUtility.getCurrentSession();
            Transaction transaction = session.beginTransaction();
           /*
            * get two User instances from the database using HQL.  This is NOT SQL.  It is object based.
            */
            Query sallyQuery = session.createQuery("select u from User as u where u.uname='sally'");
            User sallyUser = (User)sallyQuery.uniqueResult();

            Query dudeQuery = session.createQuery("select u from User as u where u.uname='dude'");
            User dudeUser = (User)dudeQuery.uniqueResult();

            // Bring all the user info and store it as an object base
            // This will through a null pointer exception because there is no Noam Chomsky anymore its Michele faulcault
            Query michelQuery = session.createQuery("select i from User as i where i.uname='Michel foucault'");
            User michelUser = (User)michelQuery.uniqueResult();

           /*
            * create a PhoneNumber instance
            */
            PhoneNumber sharedPhoneNumber = new PhoneNumber();
            sharedPhoneNumber.setPhone("(546)222-9898");
            // Lets add a phone number of Noam Chomsky
            PhoneNumber michelPhone = new PhoneNumber();
            michelPhone.setPhone("(777)777-777");

          /*
           * add the shared phone number to the noamUser
           */

            // Now bring a set of unique phones and add the one before this line
            Set<PhoneNumber> michelPhoneNum = michelUser.getPhoneNumbers();
            michelPhoneNum.add(michelPhone);


            Set<PhoneNumber> sallyPhoneNumbers = sallyUser.getPhoneNumbers();
            sallyPhoneNumbers.add(sharedPhoneNumber);

            // Share number with someone else
            Set<PhoneNumber> dudePhoneNumbers = dudeUser.getPhoneNumbers();
            dudePhoneNumbers.add(sharedPhoneNumber);

            // Now bring a set of unique phones and add the one before this line

          /*
           * inform the database that the phone number should be ready for permanent storage.
           */
            session.save(sharedPhoneNumber);
            // Here it goes Michel
            session.save(michelPhone);


            // Merge it with the rest
            session.merge(michelUser);
            session.merge(sallyUser);
            session.merge(dudeUser);
          /*
           * inform the database that the modified User instances should be ready for permanent storage.
           */
          /*
           * permanently store the changes into the database tables.
           */
            transaction.commit();
          /*
           * show that the database was updated by printing out all of the User instances created by a HQL query
           */
            showAllUsers();
        }

}
