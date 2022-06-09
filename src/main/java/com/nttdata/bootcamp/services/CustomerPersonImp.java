package com.nttdata.bootcamp.services;

import com.nttdata.bootcamp.models.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nttdata.bootcamp.models.CustomerPerson;
import com.nttdata.bootcamp.repositories.ICustomerPersonRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CustomerPersonImp implements IServiceCustomerPerson {

	@Autowired
	private ICustomerPersonRepo personRepo;
	
	@Autowired
	private RestTemplate clientRest;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Flux<CustomerPerson> findAll() {
		return personRepo.findAll().flatMap(person->{
			PersonalCredit personalCredit = clientRest.getForObject("http://service-product-personalcredit/personalCredit/findByIdCustomerPerson/"+person.getId(),PersonalCredit.class);
			SavingAccount savingAccount = clientRest.getForObject("http://service-product-savingaccount/savingAccount/findByIdCustomerPerson/"+person.getId(),SavingAccount.class);
			CurrentAccount currentAccount = clientRest.getForObject("http://service-product-currentaccount/currentAccount/findByIdCustomerAndTypeCustomer/"+person.getId()+"/customerPerson",CurrentAccount.class);
			List<FixedTermAccount> fixedTermAccounts = clientRest.getForObject("http://service-product-fixedtermaccount/fixedTermAccount/findByIdCustomerPerson/"+person.getId(),List.class);
			ProductPerson productPerson =new ProductPerson(personalCredit, savingAccount, currentAccount, fixedTermAccounts);
			person.setProductPerson(productPerson);
			return Flux.just(person);
		});
	}
	

	@Override
	public Mono<CustomerPerson> findById(String id) {
		PersonalCredit personalCredit = clientRest.getForObject("http://service-product-personalcredit/personalCredit/findByIdCustomerPerson/"+id,PersonalCredit.class);
		SavingAccount savingAccount = clientRest.getForObject("http://service-product-savingaccount/savingAccount/findByIdCustomerPerson/"+id,SavingAccount.class);
		CurrentAccount currentAccount = clientRest.getForObject("http://service-product-currentaccount/currentAccount/findByIdCustomerPerson/"+id,CurrentAccount.class);
		List<FixedTermAccount> fixedTermAccounts = clientRest.getForObject("http://service-product-fixedtermaccount/fixedTermAccount/findByIdCustomerPerson/"+id,List.class);
		ProductPerson productPerson =new ProductPerson(personalCredit, savingAccount, currentAccount, fixedTermAccounts);
		return personRepo.findById(id).flatMap(p->{
			p.setProductPerson(productPerson);
			return Mono.just(p);
		});
	}

	@Override
	public Mono<CustomerPerson> save(CustomerPerson customerPerson) {
		return personRepo.save(customerPerson);
	}

	@Override
	public Mono<Void> delete(CustomerPerson customerPerson) {
		return personRepo.delete(customerPerson);
	}


	@Override
	public Mono<CustomerPerson> findByIdDocument(String idDocument) {
		return personRepo.findByIdDocument(idDocument);
	}

}
