package rs.ac.uns.ftn.dais.domain;

import java.util.ArrayList;
import java.util.List;

public class DecompositionTreeNode {
    private Relation relation;
    private List<DecompositionTreeNode> children = new ArrayList<>();
    public static boolean INTERACTIVE_MODE = false;

    public DecompositionTreeNode(Relation relation) {
        this.relation = relation;
    }

    public Relation getRelation() {
        return relation;
    }

    public void decompose() {
        if(relation.isBCNF() || relation.getFunctionalDependencies().isEmpty()) {
            return;
        }

        Printer.print("Decomposing relation:", relation, TaskMode.DECOMPOSITION);

        FunctionalDependencySet canonicalSet = relation.getFunctionalDependencies().getCanonicalSet();
        Printer.print("Canonical set:", canonicalSet, TaskMode.DECOMPOSITION);
        relation.setFunctionalDependencies(canonicalSet);

        FunctionalDependency selected;
        if(INTERACTIVE_MODE) {
            printAvailableSets();
            selected = FunctionalDependencyParser.parse();
        } else {
            selected = relation.getDecompositionFunctionalDependency();
            Printer.print("Chosen FD:", selected, TaskMode.DECOMPOSITION);
            Printer.print("Satisfies P1 = " + relation.satisfiesP1(selected), TaskMode.DECOMPOSITION);
            Printer.print("Satisfies P2 = " + relation.satisfiesP2(selected), TaskMode.DECOMPOSITION);
            Printer.print("Satisfies P3 = " + relation.satisfiesP3(selected), TaskMode.DECOMPOSITION);
        }

        LabelSet subLabels1 = selected.getLeftSide().union(selected.getRightSide());
        Relation subRelation1 = new Relation(relation.getName(),
                                                relation.nextOrder(),
                                                subLabels1,
                                                relation.getDecompositionFirstSubset(selected, subLabels1));

        LabelSet subLabels2 = selected.getLeftSide().union(relation.getLabels().difference(selected.getRightSide()));
        Relation subRelation2 = new Relation(relation.getName(),
                relation.nextOrder() + 1,
                subLabels2,
                relation.getDecompositionSecondSubset(selected, subLabels2));

        Printer.print("-------------------------------------", TaskMode.DECOMPOSITION);
        Printer.print("Sub relations:", TaskMode.DECOMPOSITION);
        Printer.print(subRelation1, TaskMode.DECOMPOSITION);
        Printer.print(subRelation2, TaskMode.DECOMPOSITION);
        Printer.print("*************************************", TaskMode.DECOMPOSITION);

        DecompositionTreeNode child1 = new DecompositionTreeNode(subRelation1);
        DecompositionTreeNode child2 = new DecompositionTreeNode(subRelation2);
        children.add(child1);
        children.add(child2);

        child1.decompose();
        child2.decompose();
    }

    public void evaluate(RelationSet relations) {
        if(children.isEmpty()) {
            relations.add(relation);
        } else {
            for(DecompositionTreeNode child: children) {
                child.evaluate(relations);
            }
        }
    }

    private void printAvailableSets() {
        FunctionalDependencySet satisfied = relation.getFunctionalDependenciesP1();
        if(!satisfied.isEmpty()) {
            Printer.print("FDs which satisfy P1:", satisfied, TaskMode.DECOMPOSITION);
        } else {
            Printer.print("No FDs satisfy P1", TaskMode.DECOMPOSITION);
            satisfied = relation.getFunctionalDependenciesP2();
            if(!satisfied.isEmpty()) {
                Printer.print("FDs which satisfy P2:", satisfied, TaskMode.DECOMPOSITION);
            } else {
                Printer.print("No FDs satisfy P2", TaskMode.DECOMPOSITION);
                satisfied = relation.getFunctionalDependenciesP3();
                if(!satisfied.isEmpty()) {
                    Printer.print("FDs which satisfy P3:", satisfied, TaskMode.DECOMPOSITION);
                } else {
                    Printer.print("No FDs satisfy P3", TaskMode.DECOMPOSITION);
                    Printer.print("You better end the algorithm altogether :/", TaskMode.DECOMPOSITION);
                }
            }
        }
    }
}
