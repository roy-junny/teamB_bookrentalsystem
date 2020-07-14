package bookrental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
 public class BookRentalSystemController {

 @Autowired
 BookRentalSystemRepository brs;

 @Autowired
 BookListStatusRepository bls;

 @PostMapping("/rent")
 public BookRentalSystem rented(@RequestBody BookRentalSystem postBookRental) {

  Optional<BookListStatus> bookListStatusSystemOptional = bls.findById(postBookRental.getBookId());
  if (bookListStatusSystemOptional.isPresent()) {
   BookListStatus bookListStatus = bookListStatusSystemOptional.get();

   if ("IDLE".equals(bookListStatus.getRentalStatus())) {
    BookRentalSystem bookRentalSystem = new BookRentalSystem();
    bookRentalSystem.setBookId(postBookRental.getBookId());
    bookRentalSystem.setUserId(postBookRental.getUserId());
    bookRentalSystem.setRentalFee(postBookRental.getRentalFee());
    bookRentalSystem.setRentalDate(new Date());
    bookRentalSystem.setRentalStatus("RENTED");
    brs.save(bookRentalSystem);
    return bookRentalSystem;
   } else {
    System.out.println("book state is RENT!! : " + postBookRental.getBookId());
    return null;
   }
  }
  System.out.println("cant not find book ID!! : " + postBookRental.getBookId());
  return null;
 }

 @PostMapping("/return")
 public BookRentalSystem returned(@RequestBody BookRentalSystem postBookRental) {

  Optional<BookRentalSystem> bookRentalSystemOptional = brs.findById(postBookRental.getId());
  if (bookRentalSystemOptional.isPresent()) {
   BookRentalSystem bookRental = bookRentalSystemOptional.get();

   bookRental.setReturnDate(new Date());
   bookRental.setRentalStatus("RETURNED");
   brs.save(bookRental);

   Optional<BookListStatus> bookListStatusSystemOptional = bls.findById(bookRental.getBookId());
   if (bookListStatusSystemOptional.isPresent()) {
    BookListStatus bookListStatus = bookListStatusSystemOptional.get();
    bookListStatus.setRentalStatus("IDLE");
    bls.save(bookListStatus);
   }
   else {
    System.out.println("cant not find book ID!! : " + bookRental.getBookId());
   }

   return bookRental;
  }
  System.out.println("cant not find rental ID!! : " + postBookRental.getId());
  return null;
 }

 @PostMapping("/cancel")
 public BookRentalSystem cancelled(@RequestBody BookRentalSystem postBookRental) {

  Optional<BookRentalSystem> bookRentalSystemOptional = brs.findById(postBookRental.getId());
  if (bookRentalSystemOptional.isPresent()) {
   BookRentalSystem bookRental = bookRentalSystemOptional.get();

   bookRental.setReturnDate(new Date());
   bookRental.setRentalStatus("CANCELLED");
   brs.save(bookRental);
   return bookRental;
  }
  System.out.println("cant not find rental ID!! : " + postBookRental.getId());
  return null;
 }
}