package com.group1.octobots.physics;

import com.group1.octobots.Module;

/**
 * LET OP!  Development of this class has been discontinued indefinitely!!!
 *
 * An Axis-Aligned Bounding Box Tree for detecting {@link Module} collisions.
 */
public class AABBTree {

    private static final int NULL_NODE = -1;
    private static final float FATTENING_FACTOR = 0f;

    private Node root;
    private Node[] nodes;
    private int nodeCount;
    private int nodeCapacity;

    private int freeList;

    private Node[] nodeStack = new Node[20];
    private int nodeStackIndex = 0;

    public AABBTree(int size) {
        root = null;
        nodeCount = 0;
        nodeCapacity = size;
        nodes = new Node[size];

        // Make a linked list for the freeList
        for (int i = nodeCapacity - 1; i >= 0; i--) {
            nodes[i] = new Node(i);
            nodes[i].parent = (i == nodeCapacity - 1) ? null : nodes[i + 1];
            nodes[i].height = -1;
        }
        freeList = 0;

    }

    public /*final*/ void insert(Module obj) {
        AABB box = obj.body().aabb();
        final Node node = allocateNode();
        int nodeID = node.id;

        final AABB nodeBox = node.box;
        nodeBox.getMin().set(box.getMin());
        nodeBox.getMax().set(box.getMax());
        fatten(nodeBox);

        node.data = obj;

        insertLeaf(nodeID);
    }

    private final AABB combinedBox = new AABB();

    private /*final*/ void insertLeaf(int nodeID) {
        Node leaf = nodes[nodeID];
        if (root == null) {
            root = leaf;
            root.parent = null;
            return;
        }

        // find the best sibling
        AABB leafAABB = leaf.box;
        Node index = root;
        while (index.leftChild != null) {
            final Node node = index;
            Node leftChild = node.leftChild;
            Node rightChild = node.rightChild;

            float area = 0f; // TODO: 4/24/2017 PAUSE THIS WHOLE THING, we might not need it.......................
        }
    }

    private Node allocateNode() {
        if (freeList == NULL_NODE)
            throw new IndexOutOfBoundsException("Tree size exceeded");
        int nodeID = freeList;
        final Node treeNode = nodes[nodeID];
        freeList = (treeNode.parent != null) ? treeNode.parent.id : NULL_NODE;

        treeNode.parent = null;
        treeNode.leftChild = null;
        treeNode.rightChild = null;
        treeNode.height = 0;
        treeNode.data = null;
        ++nodeCount;
        return treeNode;
    }

    private static void fatten(AABB aabb) {
        V3 boxMin = aabb.getMin();
        V3 boxMax = aabb.getMax();

        boxMin.x -= FATTENING_FACTOR;
        boxMin.y -= FATTENING_FACTOR;
        boxMin.z -= FATTENING_FACTOR;

        boxMax.x += FATTENING_FACTOR;
        boxMax.y += FATTENING_FACTOR;
        boxMax.z += FATTENING_FACTOR;
    }

    public static class Node {

        public final AABB box = new AABB();

        private Node parent;

        private Node leftChild;
        private Node rightChild;
        private final int id;
        private int height;


        Module data;  //todo Optimize by extending with LeafNode and InternalNode or smthlike that

        Node(int id) {
            this.id = id;
        }

        boolean isLeaf() {
            return rightChild == null;
        }

    }
}
