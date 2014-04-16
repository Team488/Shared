package xbot.aerialassist.workers;


import xbot.aerialassist.RobotContext;
import xbot.aerialassist.autonomousworkers.DeployBothCollectorsWorker;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.systems.CollectionCore;
import xbot.common.StateMachineWorker;
import xbot.common.WorkerBase;


public class EjectBallWorker extends StateMachineWorker {
    
    private CollectionCore core = RobotContext.Get().getCollectionCore();
    
    public EjectBallWorker() {
        super("EjectBallWorker");
        
        WorkerBase ballEjectionWorker = 
                new ManualBallEjectionWorkerThatEnds(true); 
        WorkerBase raiseArmsWorker =
                new DeployBothCollectorsWorker(core.getFrontCollector(),
                CollectorDeployState.UP,
                core.getBackCollector(),CollectorDeployState.UP);
       
        int armRaiser = this.addWorker(raiseArmsWorker);
        int extendEjector = this.addWorker(ballEjectionWorker);
        int runCollectorsAsAppropriete = this.addWorker(new FieldOrientedBallEjection());
        
        this.onStart(armRaiser);
        
        this.onSuccess(armRaiser, extendEjector);
        
        this.onSuccess(extendEjector, runCollectorsAsAppropriete);
        
        // this doesn't actually ever end on it's own
        this.onSuccess(runCollectorsAsAppropriete, SUCCESS);
        
    }
    

}
