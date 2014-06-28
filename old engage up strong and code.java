					/*
					 * means we can use a simple ratio to figure out the 
					 * antecedent truth values should be
					 */
					if ((totalT < 1) && (w.getTruthval() < 1) && (maxFlag == false)) {
						percentChange = w.getPercentChange();
						
						for (int g = 0; g < antList.size(); g ++) {
							float modF = w.fwith2dp(percentChange * antList.get(g).getTruthval());
							
							if (antList.get(g).getTruthval() == 0) {
								modF = Math.abs(percentChange) / antList.size();
							}
			System.out.println("percentChange and modF are: " + percentChange + " " + modF);				
							float newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							
							if ((w.notFlag == true) && antList.get(g).getTruthval() != 0) {
								/*
								 * what percentage of the old CB truthVal was the 
								 * current antecedent? 
								 */
								float percentOldVal = antList.get(g).getTruthval() / (1 - w.oldTruthVal);
								
								/*
								 *  update the new val with the correct percentage of the 
								 *  change between the consequents old and new values		
								 */
								newtVal = w.fwith2dp((antList.get(g).getTruthval()) - 
										(percentOldVal * (w.getTruthval() - w.getOldTruthVal())));	
							}
							
				
							if (newtVal < 0) {
								newtVal = 0;
							}
							
							if (newtVal > 1) {
								newtVal = 1;
							}
System.out.println(g + " " + newtVal);						
							antList.get(g).setTruthval(w.fwith2dp(newtVal));
						}
						
					}
					
					
					if ((totalT > 1) && (w.getTruthval() < 1)) {
						percentChange = w.getPercentChange();
						float newtValTemp = 0;
						
						for (int g = 0; g < antList.size(); g ++) {
							float modF = w.fwith2dp(percentChange * antList.get(g).getTruthval());
							float newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							
							if (w.notFlag == true) {
								modF = 1 - newtVal;
								newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							}
							
				
							if ((newtVal < 0)) {
								newtVal = 0;
							}
													
							if (w.notFlag == false) {
								newtValTemp = newtValTemp + newtVal;
							}
							else {
								newtValTemp = newtValTemp + (1 - newtVal);
							}
														
							antList.get(g).setTruthval(newtVal);
						}
						
						/*
						 * check to see if we need to reduce the truth value
						 * of the antecedents further
						 */
						float difference = 0;
						float difference_div = 0;

						if (newtValTemp > w.getTruthval()) {
							difference = w.fwith2dp(newtValTemp - w.getTruthval());
							difference_div = difference = w.fwith2dp(difference / antList.size());	
						}
						
						while (difference > 0) {
							float ttemp = 0;
							
							for (int g = 0; g < antList.size(); g ++) {
								
								if (w.notFlag == false) {
									ttemp = w.fwith2dp(antList.get(g).getTruthval() - difference_div);
								}
								else {
									ttemp = w.fwith2dp(antList.get(g).getTruthval() + difference_div);
								}
								
								difference = w.fwith2dp(difference - difference_div);
								
								if (ttemp < 0) {
									difference = difference + Math.abs(ttemp);
									ttemp = 0;
								}
								
								antList.get(g).setTruthval(w.fwith2dp(ttemp));		
							}
							
						}
						
					}
					
					
					if ((totalT < 1) && (w.getTruthval() == 1)) {
						percentChange = w.getPercentChange();
						
						for (int g = 0; g < antList.size(); g ++) {
							float modF = w.fwith2dp(percentChange * antList.get(g).getTruthval());
							float newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							
							if (w.notFlag == true) {
								newtVal = 0;	
							}

							if ((newtVal > 1)) {
								newtVal = 1;
							}
							
							antList.get(g).setTruthval(newtVal);
						}
						
					}
					
				
					if ((totalT == 1) && (w.getTruthval() < 1)) {
						percentChange = w.getPercentChange();
						
						for (int g = 0; g < antList.size(); g ++) {
							float modF = w.fwith2dp(percentChange * antList.get(g).getTruthval());
							float newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							
							if (w.notFlag == true) {
								/*
								 * because totalT = 1, we know the percentage
								 * of 1 that each antecedent represents  
								 */
								newtVal = (1 - w.getTruthval()) * antList.get(g).getTruthval();
								
							}
							
							if ((newtVal > 1)) {
								newtVal = 1;
							}
							
							antList.get(g).setTruthval(newtVal);
						}
						
					}