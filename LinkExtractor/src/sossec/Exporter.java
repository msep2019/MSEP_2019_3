package sossec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import sossec.elements.SoSSecAgent;

public class Exporter {
	EcoreFactory _coreFactory;
	EcorePackage _corePackage;
	EPackage _diagramEPackage;
	EClass _nodeEClass, _edgeEClass, _objNodeEClass;
	EClass _diagramEClass, _actionEClass, _vulnerEClass, _preConditionEClass, _postConditionEClass;
	EClass _objFlowEClass;
	EAttribute _diagramName, _actionName, _vulnerName, _objFlowName;
	EAttribute _preConditionName, _postConditionName;
	EAttribute _preConditionVulner, _postConditionVulner;
	EAttribute _objFlowTarget, _objFlowSource;
	EReference _diagram_Nodes, _diagram_Edges;
	EReference _vulner_Actions, _vulner_PostConditions;
	EReference _preCondition_Vulner;
	//EObject _diagramEObject, _actionEObject, _vulnerEObject, _preConditionEObject, _postConditionEObject;
	
	private ArrayList<String> receivers = new ArrayList<>();
	
	public Exporter() {
		// Instantiate EcoreFactory and EcorePackage
		_coreFactory = EcoreFactory.eINSTANCE;
		_corePackage = EcorePackage.eINSTANCE;
	}
	
	public void buildDiagramModel() {
		createDynamicEClass();
		createDynamicEPackage();
		createDynamicEAttributes();
		
		//Place classes in diagram package
		_diagramEPackage.getEClassifiers().add(_diagramEClass);
		_diagramEPackage.getEClassifiers().add(_nodeEClass);
		_diagramEPackage.getEClassifiers().add(_edgeEClass);
		_diagramEPackage.getEClassifiers().add(_objNodeEClass);
		_diagramEPackage.getEClassifiers().add(_actionEClass);
		_diagramEPackage.getEClassifiers().add(_vulnerEClass);
		_diagramEPackage.getEClassifiers().add(_preConditionEClass);
		_diagramEPackage.getEClassifiers().add(_postConditionEClass);
		_diagramEPackage.getEClassifiers().add(_objFlowEClass);
	}
	
	public void createDynamicEClass() {
		// Create EClass instance to model ActivityDiagram class
		_diagramEClass = _coreFactory.createEClass();
		_diagramEClass.setName("ActivityDiagram");
		
		// Create EClass instance to model ActivityNode class
		_nodeEClass = _coreFactory.createEClass();
		_nodeEClass.setName("ActivityNode");
		
		// Create EClass instance to model ActivityEdge class
		_edgeEClass = _coreFactory.createEClass();
		_edgeEClass.setName("ActivityEdge");
		
		// Create EClass instance to model ObjectNode class
		_objNodeEClass = _coreFactory.createEClass();
		_objNodeEClass.setName("ObjectNode");
		_objNodeEClass.getESuperTypes().add(_nodeEClass);
		
		// Create EClass instance to model Book class
		_actionEClass = _coreFactory.createEClass();
		_actionEClass.setName("Action");
		_actionEClass.getESuperTypes().add(_nodeEClass);
		
		// Create EClass instance to model Book class
		_vulnerEClass = _coreFactory.createEClass();
		_vulnerEClass.setName("Vulnerability");
		_vulnerEClass.getESuperTypes().add(_objNodeEClass);
		
		// Create EClass instance to model Book class
		_preConditionEClass = _coreFactory.createEClass();
		_preConditionEClass.setName("PreCondition");
		
		// Create EClass instance to model Book class
		_postConditionEClass = _coreFactory.createEClass();
		_postConditionEClass.setName("PostCondition");
		
		// Create EClass instance to model ObjectFlow class
		_objFlowEClass = _coreFactory.createEClass();
		_objFlowEClass.setName("ObjectFlow");
		_objFlowEClass.getESuperTypes().add(_edgeEClass);
	}
	
	public void createDynamicEAttributes() {
		/**
		 *  EClass Diagram 
		 */
		_diagramName = _coreFactory.createEAttribute();
		_diagramName.setName("name");
		_diagramName.setEType(_corePackage.getEString());
		_diagramEClass.getEStructuralFeatures().add(_diagramName);
		
		_diagram_Nodes = _coreFactory.createEReference();
		_diagram_Nodes.setName("node");
		_diagram_Nodes.setEType(_nodeEClass);
		_diagram_Nodes
				.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		_diagram_Nodes.setContainment(true);
		_diagramEClass.getEStructuralFeatures().add(_diagram_Nodes); 
		
		_diagram_Edges = _coreFactory.createEReference();
		_diagram_Edges.setName("edge");
		_diagram_Edges.setEType(_edgeEClass);
		_diagram_Edges
				.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		_diagram_Edges.setContainment(true);
		_diagramEClass.getEStructuralFeatures().add(_diagram_Edges); 
		
		/**
		 *  EClass Action
		 */
		_actionName = _coreFactory.createEAttribute();
		_actionName.setName("name");
		_actionName.setEType(_corePackage.getEString());
		_actionEClass.getEStructuralFeatures().add(_actionName);
		
		/**
		 *  EClass Vulnerability
		 */
		_vulnerName = _coreFactory.createEAttribute();
		_vulnerName.setName("name");
		_vulnerName.setEType(_corePackage.getEString());
		_vulnerEClass.getEStructuralFeatures().add(_vulnerName);
		
		_vulner_Actions = _coreFactory.createEReference();
		_vulner_Actions.setName("menaces");
		_vulner_Actions.setEType(_actionEClass);
		_vulner_Actions
				.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		_vulner_Actions.setContainment(true);
		_vulnerEClass.getEStructuralFeatures().add(_vulner_Actions);
		
		_vulner_PostConditions = _coreFactory.createEReference();
		_vulner_PostConditions.setName("results_in");
		_vulner_PostConditions.setEType(_postConditionEClass);
		_vulner_PostConditions
				.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		_vulner_PostConditions.setContainment(true);
		_vulnerEClass.getEStructuralFeatures().add(_vulner_PostConditions);
		
		/**
		 *  EClass PreCondition
		 */
		_preConditionName = _coreFactory.createEAttribute();
		_preConditionName.setName("name");
		_preConditionName.setEType(_corePackage.getEString());
		_preConditionEClass.getEStructuralFeatures().add(_preConditionName);
		
		_preCondition_Vulner = _coreFactory.createEReference();
		_preCondition_Vulner.setName("results_in");
		_preCondition_Vulner.setEType(_vulnerEClass);
		_preCondition_Vulner
				.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		_preCondition_Vulner.setContainment(true);
		_preConditionEClass.getEStructuralFeatures().add(_preCondition_Vulner);
		
		_preConditionVulner = _coreFactory.createEAttribute();
		_preConditionVulner.setName("activates");
		_preConditionVulner.setEType(_corePackage.getEString());
		_preConditionEClass.getEStructuralFeatures().add(_preConditionVulner);
		
		/**
		 *  EClass PostCondition
		 */
		_postConditionName = _coreFactory.createEAttribute();
		_postConditionName.setName("name");
		_postConditionName.setEType(_corePackage.getEString());
		_postConditionEClass.getEStructuralFeatures().add(_postConditionName);
		
		_postConditionVulner = _coreFactory.createEAttribute();
		_postConditionVulner.setName("vulnerability");
		_postConditionVulner.setEType(_corePackage.getEString());
		_postConditionEClass.getEStructuralFeatures().add(_postConditionVulner);
		
		/**
		 *  EClass OBjectlFlow
		 */
		/*
		_objFlowName = _coreFactory.createEAttribute();
		_objFlowName.setName("name");
		_objFlowName.setEType(_corePackage.getEString());
		_objFlowEClass.getEStructuralFeatures().add(_objFlowName);
		*/
		
		_objFlowTarget = _coreFactory.createEAttribute();
		_objFlowTarget.setName("target");
		_objFlowTarget.setEType(_corePackage.getEString());
		_objFlowEClass.getEStructuralFeatures().add(_objFlowTarget);
		
		_objFlowSource = _coreFactory.createEAttribute();
		_objFlowSource.setName("source");
		_objFlowSource.setEType(_corePackage.getEString());
		_objFlowEClass.getEStructuralFeatures().add(_objFlowSource);
	}
	
	public void createDynamicEPackage() {
		// Instantiate EPackage and provide unique URI to identify the package
		// instance
		_diagramEPackage = _coreFactory.createEPackage();
		_diagramEPackage.setName("soSSecProfileEcoreRepresentation");
		_diagramEPackage.setNsPrefix("soSSec");
		_diagramEPackage
				.setNsURI("http://www.example.org/soSSecProfileEcoreRepresentation");
	}
	
	public void createDynamicInstances(XMIResource resource, HashMap<String, SoSSecAgent> agents) {
		// Obtain factory instance from DiagramPackage
		EFactory diagramFactoryInstance = _diagramEPackage.getEFactoryInstance();
		
		EObject _diagramEObject = diagramFactoryInstance.create(_diagramEClass);
		_diagramEObject.eSet(_diagramName, "Cascading Diagram");
		resource.getContents().add(_diagramEObject);
		
		// Agent loop
		agents.forEach((agentName, agent) -> {
			
			// Behaviour loop
			agent.getBehaviours().forEach((behaviourName, behaviour) -> {
				EObject _actionEObject = diagramFactoryInstance.create(_actionEClass);
				_actionEObject.eSet(_actionName, behaviour.getName());
				
				resource.getContents().add(_actionEObject);
				resource.setID(_actionEObject, behaviour.getUUID());
				
				// Vulnerability loop
				behaviour.getVulnerabilities().forEach((vulnerName, vulner) -> {
					EObject _vulnerEObject = diagramFactoryInstance.create(_vulnerEClass);
					_vulnerEObject.eSet(_vulnerName, vulner.getName());
					resource.getContents().add(_vulnerEObject);
					resource.setID(_vulnerEObject, vulner.getUUID());
					
					EObject _objFlowEObject = diagramFactoryInstance.create(_objFlowEClass);
					_objFlowEObject.eSet(_objFlowSource, vulner.getUUID());
					_objFlowEObject.eSet(_objFlowTarget, behaviour.getUUID());
					resource.getContents().add(_objFlowEObject);
					resource.setID(_objFlowEObject, EcoreUtil.generateUUID());
					
					// Condition loop
					vulner.getConditions().forEach((conditionName, condition) -> {
						EObject _conditionEObject = null;
						if (condition.getType() == "PreCondition") {
							_conditionEObject = diagramFactoryInstance.create(_preConditionEClass);
							_conditionEObject.eSet(_preConditionName, condition.getName());
							_conditionEObject.eSet(_preConditionVulner, vulner.getUUID());
						} else if (condition.getType() == "PostCondition") {
							_conditionEObject = diagramFactoryInstance.create(_postConditionEClass);
							_conditionEObject.eSet(_postConditionName, condition.getName());
							_conditionEObject.eSet(_postConditionVulner, vulner.getUUID());
							
							behaviour.getReceivers().forEach((receiverName, receiver) -> {
								
								if (!receivers.contains(receiver.getName())) {
									receiver.getVulnerabilities().forEach((receiverVulnerName, receiverVulner) -> {
										receiverVulner.getConditions().forEach((receiverConditionName, receiverCondition) -> {
											if (receiverCondition.getType() == "PreCondition" && condition.getName().equals(receiverCondition.getName())) {
												EObject _objFlowEObjectAction = diagramFactoryInstance.create(_objFlowEClass);
												_objFlowEObjectAction.eSet(_objFlowSource, behaviour.getUUID());
												_objFlowEObjectAction.eSet(_objFlowTarget, receiver.getUUID());
												resource.getContents().add(_objFlowEObjectAction);
												resource.setID(_objFlowEObjectAction, EcoreUtil.generateUUID());
												
												receivers.add(receiver.getName());
											}
										});
									});
								}
							});
						}
						
						if (_conditionEObject != null) {
							resource.getContents().add(_conditionEObject);
							resource.setID(_conditionEObject, condition.getUUID());
						}
						
					});
				});
			});
		});
	}
	
	public void exportXMI(HashMap<String, SoSSecAgent> agents) throws IOException{
		
		System.out.println("Export XMI file...");
		
		ResourceSet resourceSet = new ResourceSetImpl();

		// Register XML Factory implementation to handle .xml files
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());

		// Create empty resource with the given URI
		XMIResource resource = (XMIResource)resourceSet.createResource(URI
				.createURI("./model.xmi"));

		buildDiagramModel();
		
		createDynamicInstances(resource, agents);
		
		// Add bookStoreObject to contents list of the resource
		//resource.getContents().add(_diagramEPackage);		
		
		try {
			resource.save(null);
			
			System.out.println("Finish XMI file...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

