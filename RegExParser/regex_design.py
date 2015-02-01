class RegExTreeBasis:
    
    '''
    A base class representing a tree of valid regular expression values
    '''

    def __init__(self, value=None, left=None, right=None):
        '''(self, str, tree or str, tree or str) -> None
        Create a new empty regular expression tree using value, left and right.
        
        value --- initial node value
        left --- initial left branch node value
        right --- initial right branch node value

        >>> reg_ex = RegExTreeBasis('0', '1', '2')
        >>> reg_ex.value
        '0'
        >>> reg_ex.left
        RegExLeafNode('1')
        >>> reg_ex.right
        RegExLeafNode('2')
        '''

        self.value = value

        if type(left) is str:
            self.left = RegExLeafNode(left)
        elif type(left) in (RegExStarNode, RegExDotNode, RegExBarNode,
                             RegExLeafNode):
            self.left = left
        elif left is None:
            self.left = left
        else:
            print('Invalid input class for left value')

        if type(right) is str:
            self.right = RegExLeafNode(right)
        elif type(right) in (RegExStarNode, RegExDotNode, RegExBarNode,
                             RegExLeafNode):
            self.right = right
        elif right is None:
            self.right = right
        else:
            print('Invalid input class for right value')

    def get_value(self):
        '''
        Return the value attribute of self
        '''
        
        return self._value

    def set_value(self, value):
        '''
        Set the value attribute of self if not already set
        '''
        
        if '_value' in dir(self):
            print('You cannot reset this attribute')
        else:
            self._value = value

    value = property(get_value, set_value)

    def get_left(self):
        '''
        Return the value of the left attribute of self
        '''
        
        return self._left

    def set_left(self, left):
        '''
        Set the left attribute of self if not already set
        '''
        
        if '_left' in dir(self):
            print('You cannot reset this attribute')
        else:
            self._left = left

    left = property(get_left, set_left)

    def get_right(self):
        '''
        Return the value of the right atribute of self
        '''
        
        return self._right

    def set_right(self, right):
        '''
        Set the right attribute of self if not already set
        '''

        if '_right' in dir(self):
            print("You cannot reset this attribute")
        else:
            self._right = right

    right = property(get_right, set_right)

    def __eq__(self, other):
        ''' (tree_object, object) -> bool
        Return whether or not self is equivalent to other

        >>> r = RegExStarNode('0')
        >>> r2 = RegExDotNode('1', '2')
        >>> r == r2
        False
        >>> r = RegExBarNode('', '')
        >>> r2 = RegExBarNode('', '')
        >>> r == r2
        True
        '''

        #return repr(self) == repr(other)

        
        if type(self) != type(other):
            return False
        if self.value != other.value:
            return False
        eq = True
        if self.left is None and other.left:
            return False
        else:
            eq = self.left.__eq__(other.left)
            if not eq:
                return self.left.__eq__(other.left)
        if self.right is None and other.right:
            return False
        else:
            return self.right.__eq__(other.right)
        

class RegExStarNode(RegExTreeBasis):

    '''
    Regular expression tree that has a set value node of '*'
    '''

    def __init__(self, left=None):
        '''(self, tree or str) -> None
        Create a new RegExStarNode with a fixed value of '*' and using left
        while hardsetting None as right

        left --- the left child, in this case representing the only branch
        of the tree

        >>> reg_ex = RegExStarNode('0')
        >>> reg_ex.value
        '*'
        >>> reg_ex.left
        RegExLeafNode('0')
        >>> reg_ex.right
        >>> 
        '''

        RegExTreeBasis.__init__(self, '*', left, None)

    def __repr__(self):
        '''(RegExStarNode) -> str
        Return a string representation of self

        >>> reg_ex = RegExStarNode('')
        >>> reg_ex
        RegExStarNode(RegExLeafNode('e'))
        '''

        return 'RegExStarNode({})'.format(repr(self.left))
    
            
class RegExDotNode(RegExTreeBasis):

    '''
    Regular expression tree that has a set value node of '.'
    '''

    def __init__(self, left=None, right=None):
        '''(self, tree or str) -> None
        Create a new RegExDotNode with a fixed value of '.' and using left
        and right

        left --- the left child
        right --- the right child

        >>> reg_ex = RegExDotNode('0', '1')
        >>> reg_ex.value
        '.'
        >>> reg_ex.left
        RegExLeafNode('0')
        >>> reg_ex.right
        RegExLeafNode('1')
        '''

        RegExTreeBasis.__init__(self, '.', left, right)

    def __repr__(self):
        '''(RegExDotNode) -> str
        Return a string representation of self

        >>> reg_ex = RegExDotNode('0', '1')
        >>> reg_ex
        RegExDotNode(RegExLeafNode('0'), RegExLeafNode('1'))
        '''

        return 'RegExDotNode({}, {})'.format(repr(self.left), repr(self.right))


class RegExBarNode(RegExTreeBasis):

    '''
    Regular expression tree that has a set value node of '|'
    '''

    def __init__(self, left=None, right=None):
        '''(self, tree or str) -> None
        Create a new RegExBarNode with a fixed value of '|' and using left
        and right

        left --- the left child
        right --- the right child

        >>> reg_ex = RegExBarNode('0', '1')
        >>> reg_ex.value
        '|'
        >>> reg_ex.left
        RegExLeafNode('0')
        >>> reg_ex.right
        RegExLeafNode('1')
        '''

        RegExTreeBasis.__init__(self, '|', left, right)

    def __repr__(self):
        '''(RegExBarNode) -> str
        Return a string representation of self

        >>> reg_ex = RegExBarNode('0', '1')
        >>> reg_ex
        RegExBarNode(RegExLeafNode('0'), RegExLeafNode('1'))
        '''

        return 'RegExBarNode({}, {})'.format(repr(self.left), repr(self.right))
    

class RegExLeafNode(RegExTreeBasis):

    '''
    Regular expression tree that has a set value node of '|'
    '''

    def __init__(self, value=None):
        '''(self, tree or str) -> None
        Create a new RegExLeafNode taking only a value parameter and setting
        both left and right as None to represent a leaf node of a tree. The
        value of both left and right must be in the set of
        ('0', '1', '2', '', 'e')

        value --- the value attribute of the leaf

        >>> reg_ex = RegExLeafNode('')
        >>> reg_ex.value
        'e'
        >>> reg_ex.left
        >>> reg_ex.right
        >>> 
        '''

        if value in ('0', '1', '2', '', 'e'):
            if value == '':
                value = 'e'
            RegExTreeBasis.__init__(self, value, None, None)
        else:
            print("Value must be '0', '1', '2' or ''")

    def __repr__(self):
        '''(RegExLeafNode) -> str
        Return a string representation of self

        >>> reg_ex = RegExLeafNode('')
        >>> reg_ex
        RegExLeafNode('e')
        '''
        
        return 'RegExLeafNode({})'.format(repr(self.value))
    
