package com.abhi.TrainReservation.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.abhi.TrainReservation.model.Passenger;
import com.abhi.TrainReservation.model.Ticket;
import com.abhi.TrainReservation.model.Train;
import com.abhi.TrainReservation.model.TrainDao;
import com.abhi.TrainReservation.model.User;
import com.abhi.TrainReservation.repository.UserRepository;
import com.abhi.TrainReservation.service.TrainService;

@Controller
public class HomeController {

	@Autowired
	private TrainService trainService;

	@Autowired
	UserRepository userRepository;

	@PostMapping("/register")
	public String addUser(User user) {
		userRepository.save(user);
		return "login";
	}
	@RequestMapping("/adminpage")
    public String adminpage() {
        return "adminpage";
    }

    @RequestMapping("/adminpannel")
    public String adminpannel() {
        return "adminpannel";
    }
    
    @RequestMapping(path ="/processTicket", method = RequestMethod.POST)
    public String processTicket(HttpServletRequest request, Model m) {

        String src = request.getParameter("source_place");
        String dest = request.getParameter("destination_place");

        int nop = 0;
        List<Train> allTrains = trainService.getAllTrains();
        m.addAttribute("allTrains", allTrains);
 
        TrainDao td = new TrainDao();
        Ticket ticket = null;
        Train train = null;
        try {
 
            train = td.findTrain(src,dest);
            System.out.println(train);
            if (train == null) {
                m.addAttribute("error_msg", "Train with given source and destination  not found.");
                return "home";
            }
        }catch(Exception e) {
            System.out.println(e);
        }
        return "home";
    }
    
	@PostMapping("/login")
	public String loginUser(@RequestParam String userName, @RequestParam String password) {
		Optional<User> userOptional = userRepository.findByUserNameAndPassword(userName, password);

		if (userOptional.isPresent()) {
			return "redirect:/home";
		} else {
			return "redirect:/login";
		}

	}

	@GetMapping(value = "/home")
	public ModelAndView getHomePage() {

		List<Train> allTrains = trainService.getAllTrains();

		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("allTrains", allTrains);

		return modelAndView;
	}

	@GetMapping(value = "/gateway")
	public ModelAndView getGateWayPage() {

		ModelAndView modelAndView = new ModelAndView("gateway");

		return modelAndView;
	}

	@GetMapping(value = { "/", "/login" })
	public ModelAndView getLoginPage() {

		ModelAndView modelAndView = new ModelAndView("login");

		return modelAndView;
	}

	@GetMapping("/bookTrain/{trainNo}")
	public ModelAndView getBookTrainPage(@PathVariable int trainNo) {

		Optional<Train> trainOptional = trainService.getTrainByNumber(trainNo);

		ModelAndView modelAndView = new ModelAndView("bookTrain");

		if (trainOptional.isPresent()) {
			Train train = trainOptional.get();
			modelAndView.addObject("train", train);
		}

		return modelAndView;
	}

	@PostMapping("/bookTrain/{trainNo}")
	public ModelAndView doBooking(Passenger passenger, String travelDate, @PathVariable int trainNo) {

		Optional<Train> trainOptional = trainService.getTrainByNumber(trainNo);

		ModelAndView modelAndView = new ModelAndView("printTicket");

		if (trainOptional.isPresent()) {
			Train train = trainOptional.get();

			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

			DateTime dateTime = DateTime.parse(travelDate, formatter);

			int date = dateTime.getDayOfMonth();
			int month = dateTime.getMonthOfYear();
			int year = dateTime.getYear();

			Ticket ticket = new Ticket(new Date(year, month, date), train);

			ticket.addPassenger(passenger.getName(), passenger.getAge(), passenger.getGender(), passenger.getAadharNumber());

			ticket.getTotalFare();

			modelAndView.addObject("ticket", ticket);

		}

		return modelAndView;
	}

}