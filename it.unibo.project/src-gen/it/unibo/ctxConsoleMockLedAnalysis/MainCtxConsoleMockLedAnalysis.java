/* Generated by AN DISI Unibo */ 
package it.unibo.ctxConsoleMockLedAnalysis;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxConsoleMockLedAnalysis  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	String webDir = "./srcMore/it/unibo/ctxConsoleMockLedAnalysis";
	return QActorContext.initQActorSystem(
		"ctxconsolemockledanalysis", "./srcMore/it/unibo/ctxConsoleMockLedAnalysis/robotroombaanalysis.pl", 
		"./srcMore/it/unibo/ctxConsoleMockLedAnalysis/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}
