package com.hidglobal.managedservices.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hidglobal.managedservices.dao.PartNumberDAO;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.vo.PartNumber;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.SearchPartNumberResponse;
import com.hidglobal.managedservices.vo.SearchRequest;

/**
 * 
 * This class act as a Mediator for all the PartNumber related requests. It has
 * the implementation of Business Logic of where the Request needs to delegated
 * for the operation to be executed successfully.This class handles the
 * following requests 1.Get PartNumber 2.List PartNumber 3.Search PartNumber
 * 
 */
@Service
public class PartNumberManagement {

	@Autowired
	private PartNumberDAO partNumberDAO;
/**
 * This method routes Get PartNumber requests to PartNumberDAO Class.
 * @param filterByIds
 * @return
 * @throws DBException
 */
	public PartNumber getPartNumberDetails(QueryFilterId filterByIds)
			throws DBException {
		// TODO Auto-generated method stub
		return partNumberDAO.fetchPartNumberDetails(filterByIds);
	}
/**
 * This method routes List PartNumber requests to PartNumberDAO Class.
 * @param filterByIds
 * @return
 * @throws DBException
 */
	public SearchPartNumberResponse listPartNumbers(QueryFilterId filterByIds)
			throws DBException {
		// TODO Auto-generated method stub
		return partNumberDAO.listPartNumbers(filterByIds);
	}
/**
 *  This method routes Search PartNumber requests to PartNumberDAO Class.
 * @param searchRequest
 * @param filterByIds
 * @return
 * @throws Exception
 */
	public SearchPartNumberResponse searchPartNumber(
			SearchRequest searchRequest, QueryFilterId filterByIds)
			throws Exception {
		// TODO Auto-generated method stub
		return partNumberDAO
				.searchPartNumberDetails(searchRequest, filterByIds);
	}

	public PartNumberDAO getPartNumberDAO() {
		return partNumberDAO;
	}

	public void setPartNumberDAO(PartNumberDAO partNumberDAO) {
		this.partNumberDAO = partNumberDAO;
	}

}
