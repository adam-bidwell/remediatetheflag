/*
 *  
 * REMEDIATE THE FLAG
 * Copyright 2018 - Andrea Scaduto 
 * remediatetheflag@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.remediatetheflag.global.actions.auth.management.monitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.remediatetheflag.global.actions.IAction;
import com.remediatetheflag.global.messages.MessageGenerator;
import com.remediatetheflag.global.model.Team;
import com.remediatetheflag.global.model.User;
import com.remediatetheflag.global.persistence.HibernatePersistenceFacade;
import com.remediatetheflag.global.utils.Constants;

public class GetTeamDetails extends IAction {

	private HibernatePersistenceFacade hpc = new HibernatePersistenceFacade();

	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		User sessionUser = (User) request.getSession().getAttribute(Constants.ATTRIBUTE_SECURITY_CONTEXT);		
		JsonObject json = (JsonObject) request.getAttribute(Constants.REQUEST_JSON);
		JsonElement jsonElement = json.get(Constants.ACTION_PARAM_NAME);
		String name = jsonElement.getAsString();
		Team team = hpc.getTeamFromName(name,sessionUser.getManagedOrganizations());
		if(null==team){
			MessageGenerator.sendErrorMessage("NotFound", response);
			return;
		}
		if(sessionUser.getRole().equals(Constants.ROLE_TEAM_MANAGER)){
			if(!team.getManagers().contains(sessionUser)){
				MessageGenerator.sendErrorMessage("NotFound", response);
				return;
			}
		}
		MessageGenerator.sendTeamDetailsMessage(team,response);
	}

}
