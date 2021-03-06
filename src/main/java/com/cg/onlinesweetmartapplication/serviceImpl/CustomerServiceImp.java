package com.cg.onlinesweetmartapplication.serviceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.onlinesweetmartapplication.entities.Cart;
import com.cg.onlinesweetmartapplication.entities.Customer;
import com.cg.onlinesweetmartapplication.entities.SweetItem;
import com.cg.onlinesweetmartapplication.entities.SweetOrder;
import com.cg.onlinesweetmartapplication.exceptions.CartNotFoundException;
import com.cg.onlinesweetmartapplication.exceptions.CustomerNotFoundException;
import com.cg.onlinesweetmartapplication.model.CustomerDTO;
import com.cg.onlinesweetmartapplication.repository.ICustomerRepository;
import com.cg.onlinesweetmartapplication.service.ICustomerService;
import com.cg.onlinesweetmartapplication.utils.CustomerUtils;

@Service
public class CustomerServiceImp implements ICustomerService{
    @Autowired
    public ICustomerRepository repository;
	@Override
	public CustomerDTO addCustomer(Customer customer) {
		if(customer==null)
			return null;
		return CustomerUtils.convertToCustomerDto(repository.save(customer));
	}
	
	@Override
	public CustomerDTO updateCustomer(Customer customer)throws CustomerNotFoundException {
	if(customer==null)
		{
		throw new CustomerNotFoundException("Invalid id");
	}
	else {
		return CustomerUtils.convertToCustomerDto(repository.save(customer));
	}
		
	}

	@Override
	public CustomerDTO cancelCustomer(int customerId) throws CustomerNotFoundException 
	{
		Customer existingCustomer = repository.findById(customerId).orElse(null);
		if (existingCustomer == null) {
			throw new CustomerNotFoundException("Invalid id");
		}
		else {
			repository.delete(existingCustomer);
			return CustomerUtils.convertToCustomerDto(existingCustomer);
		}
	}
	
	@Override
	public List<CustomerDTO> showAllCustomers() {
		List<Customer> listCustomers = repository.findAll();
		return CustomerUtils.convertToCustomerDtoList(listCustomers);
	}

	public List<CustomerDTO> showAllCustomers(int customerId) {
		List <Customer> listCustomers = new ArrayList<Customer>();
		Optional<Customer> customerOptional = repository.findById(customerId);
		if (customerOptional.isPresent())
			listCustomers.add(customerOptional.get());
		return CustomerUtils.convertToCustomerDtoList(listCustomers);
	}
	
	
	public static boolean validateCust(Customer customer) throws CustomerNotFoundException {
		boolean flag;
		if (customer == null  ) {
			flag = false;
		}
		else if (!(validateCustomerUserId(customer) && validateCustomerUsername(customer) &&  validateCustomerSetSweetOrders(customer) && validateCustomerSweetItem(customer))) {
			flag = false;
		}
		else {
			flag = true;
		}
		return flag;
	}
	

	public static boolean validateCustomerUsername(Customer customer) throws CustomerNotFoundException {
		boolean flag = true;
		if(customer.getUsername().matches("^[a-zA-Z0-9 ]+$") && customer.getUsername().length()>2)
			flag=true;
		else 
			throw new CustomerNotFoundException("Enter a valid name");
		return flag; 
			
	}

	public static boolean validateCustomerUserId(Customer customer) throws CustomerNotFoundException {
		boolean flag = true;
		if(customer.getUserId()>0)
			flag=true;
		else
			throw new CustomerNotFoundException("Enter valid user id");
		return flag;
	}
	

	public static boolean validateCustomerSetSweetOrders(Customer customer) {
		boolean flag = true;
		Set<SweetOrder> setSweetOrder = customer.getSweetOrders();
		if (setSweetOrder == null || setSweetOrder.size() == 0)
			flag = false;
		return flag;
	}
		public static boolean validateCustomerSweetItem(Customer customer) {
		boolean flag = true;
		List<SweetItem> listSweetItem = customer.getSweetItems();
		if (listSweetItem == null || listSweetItem.size() == 0)
			flag = false;
		return flag;
	}
		
		public static boolean validateCartId(Cart cart) throws CartNotFoundException
		{
			boolean flag=false;
			if(cart.getCartId()>0)
				flag=true;
			else
				throw new CartNotFoundException("Not a valid cart id");
			return flag;
		}

}