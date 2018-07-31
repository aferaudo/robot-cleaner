/* Generated by AN DISI Unibo */ 
package it.unibo.mindrobotanalysis;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractMindrobotanalysis extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractMindrobotanalysis(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/mindrobotanalysis/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/mindrobotanalysis/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("waitPlan",waitPlan);
	    	stateTab.put("handleEvent",handleEvent);
	    	stateTab.put("handleMsg",handleMsg);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "mindrobotanalysis tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	temporaryStr = "\"Mind robot ready\"";
	    	println( temporaryStr );  
	    	//switchTo waitPlan
	        switchToPlanAsNextState(pr, myselfName, "mindrobotanalysis_"+myselfName, 
	              "waitPlan",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitPlan = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_waitPlan",0);
	     pr.incNumIter(); 	
	    	String myselfName = "waitPlan";  
	    	//bbb
	     msgTransition( pr,myselfName,"mindrobotanalysis_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleEvent"), stateTab.get("handleMsg") }, 
	          new String[]{"true","E","constraint", "true","M","moveRobot" },
	          3600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitPlan){  
	    	 println( getName() + " plan=waitPlan WARNING:" + e_waitPlan.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitPlan
	    
	    StateFun handleEvent = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleEvent",-1);
	    	String myselfName = "handleEvent";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("constraint(temp,V)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("constraint") && 
	    		pengine.unify(curT, Term.createTerm("constraint(CONSTRAINT,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg  ="currentTemperature(X)";
	    			String parg1 ="currentTemperature(V)";
	    			/* ReplaceRule */
	    			parg = updateVars(Term.createTerm("constraint(CONSTRAINT,VALUE)"),  Term.createTerm("constraint(temp,V)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			parg1 = updateVars(Term.createTerm("constraint(CONSTRAINT,VALUE)"),  Term.createTerm("constraint(temp,V)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg1);
	    			if( parg != null && parg1 != null  ) replaceRule(parg, parg1);	    		  					
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?checkTemperature(hot)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"usercmd(CMD)","usercmd(consoleGui(stopBot))", guardVars ).toString();
	    	sendMsg("execMoveRobot","robotexecutoranalysis", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"mindrobotanalysis_"+myselfName,false,true);
	    }catch(Exception e_handleEvent){  
	    	 println( getName() + " plan=handleEvent WARNING:" + e_handleEvent.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleEvent
	    
	    StateFun handleMsg = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleMsg",-1);
	    	String myselfName = "handleMsg";  
	    	printCurrentMessage(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(consoleGui(startBot))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRobot") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?checkTemperature(cold)" )) != null ){
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"usercmd(CMD)","usercmd(consoleGui(startBot))", guardVars ).toString();
	    		sendMsg("execMoveRobot","robotexecutoranalysis", QActorContext.dispatch, temporaryStr ); 
	    		}
	    		else{ temporaryStr = "\"Too hot to work\"";
	    		temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    		println( temporaryStr );  
	    		}};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(consoleGui(stopBot))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRobot") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?checkTemperature(cold)" )) != null ){
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"usercmd(CMD)","usercmd(consoleGui(stopBot))", guardVars ).toString();
	    		sendMsg("execMoveRobot","robotexecutoranalysis", QActorContext.dispatch, temporaryStr ); 
	    		}
	    		else{ temporaryStr = "\"Too hot to work\"";
	    		temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    		println( temporaryStr );  
	    		}};//actionseq
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"mindrobotanalysis_"+myselfName,false,true);
	    }catch(Exception e_handleMsg){  
	    	 println( getName() + " plan=handleMsg WARNING:" + e_handleMsg.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleMsg
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
