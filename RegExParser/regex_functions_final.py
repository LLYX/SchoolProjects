"""
# Copyright 2013 Nick Cheng, Brian Harrington, Danny Heap, Leon Xu, Xin Serena
# Wen 2013, 2014
# Distributed under the terms of the GNU General Public License.
#
# This file is part of Assignment 1, CSC148, Winter 2014
#
# This is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This file is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this file.  If not, see <http://www.gnu.org/licenses/>.
"""

# Do not change this import statement, or add any of your own!
from regextree import RegexTree, Leaf, StarTree, DotTree, BarTree

# Do not change any of the class declarations above this comment
# Student code below this comment.

def is_regex(s: str) -> bool:
    """Given s return True iff s is a proper regular expression. Else return
    False.
    >>> is_regex("(((0.2)|2*).(0*.2))")
    True
    >>> is_regex("(((0.2)|8.0 )")
    False
    """
    
    if s:
        if s.count("(") == s.count(")"):
            if s.startswith("(") and s.endswith(")"):
                if s[1] == "(":
                    index = find_bracket_end(s[:-1], 1) + 1
                    if s[index] == "*":
                        index += 1
                    if s[index] in ".|":
                        return is_regex(s[1:index]) and\
                               is_regex(s[index + 1:-1])
                elif "." in s[2:4] or "|" in s[2:4]:
                    index = first_cond_index(s)
                    return is_regex(s[1:index]) and is_regex(s[index + 1:-1])
            elif len(s) == 1:
                return s in "012e"
            elif s[-1] == "*":
                return is_regex(s[:-1])
    return False
    
def find_bracket_end(s: str, start: int = 0) -> int:
    """Given s (and start if the element at the 0 index is not a bracket)
    return the index where the first bracket ends.
    >>> s = "(1(3(5)))9(11(13))"
    >>> find_bracket_end(s)
    8
    >>> s = "(1)9(11(13))"
    >>> find_bracket_end(s)
    2
    """
    counter = 0
    for index in range(start, len(s)):
        if s[index] == "(":
            counter += 1
        elif s[index] == ")":
            counter -= 1
        if counter == 0:
            return index

def first_cond_index(s: str) -> int:
    """Given s return the index of the first condition ("." or "|") in the
    range of 2 to 3 inclusive.

    >>> s = "(1*.2)"
    >>> first_cond_index(s)
    3
    >>> s = "(2|(e|1))"
    >>> first_cond_index(s)
    2
    """
    if s[2] == "*":
        return 3
    else:
        return 2

def all_regex_permutations(s: str) -> set:
    """Given s return a set of permutations of s that are also valid regular 
    expressions
    >>> s = "0.23()*"
    >>> p = all_regex_permutations(s)
    >>> p == {'(0*.2)', '(2.0)*', '(2*.0)', '(0.2*)', '(0.2)*', '(2.0*)'}
    True
    >>> s = None
    >>> all_regex_permutations(s)
    {}
    """
    if not s:
            return {}    
    for c in s:
            if c not in '012e*.|()':
                s = s.replace(c, '')    
    
    combinations = permutate(s)
    
    combinations[:] = (e for e in combinations if is_regex(e))
    
    if not combinations:
        return {}
    
    return set(combinations)

def permutate(s: str) -> list:
    """Given s return a list with all its permutations.
    >>> permutate("123")
    ['123', '213', '231', '132', '312', '321']
    >>> permutate("ab")
    ['ab', 'ba']
    """
    if len(s) < 2:
        return [s]
    
    rest = permutate(s[1:])
    first_char = s[0]
    permutations = []
                       
    for string in rest:
        for i in range(len(string) + 1):
            permutations.append(string[:i] + first_char + string[i:])
    
    return permutations
            
def regex_match(r: "RegexTree", s: str) -> bool:
    """ Return True iff s matches the regular expression tree rooted at r.
    >>> r = StarTree(DotTree(Leaf('0'), Leaf('e')))
    >>> s = "00000"
    >>> regex_match(r, s)
    True
    >>> r = DotTree(Leaf('e'), Leaf('e'))
    >>> s = ""
    >>> regex_match(r, s)
    True
    """

    if type(r) is Leaf:
        if len(s) == 1 and s in '012':
            return r.symbol == s
        elif s is '':
            return r.symbol is 'e'
    elif type(r) is StarTree:
        if len(s) > 0:
            for i in range(len(s) + 1):
                if regex_match(r.children[0], s[:i]):
                    if regex_match(r, s[i:]):
                        return True
        elif s is '':
            return True
    elif type(r) is BarTree:
        return regex_match(r.children[0], s) or regex_match(r.children[1], s)
    elif type(r) is DotTree:
        for i in range(len(s) + 1):
            if regex_match(r.children[0], s[:i]):
                if regex_match(r.children[1], s[i:]):
                    return True

    return False
    
def build_regex_tree(regex: str) -> "RegexTree":
    """Given regex, a valid regular expression regex, build the corresponding
    regular expression tree and returns its root.
    >>> regex = "(0.1)*"
    >>> build_regex_tree(regex)
    StarTree(DotTree(Leaf('0'), Leaf('1')))
    >>> regex = "(0*|(1.2)*)"
    >>> build_regex_tree(regex)
    BarTree(StarTree(Leaf('0')), StarTree(DotTree(Leaf('1'), Leaf('2'))))
    """

    if regex in ('0', '1', '2', 'e'):
        return Leaf(regex)
    elif regex[-1] is '*':
        return StarTree(build_regex_tree(regex[:-1]))
    elif (regex[0] is '(' and regex[-1] is ')' and
          len(regex) is 5 and ('.' in regex or '|' in regex)):
        if '.' in regex:
            return DotTree(build_regex_tree(regex[1:2]),
                           build_regex_tree(regex[3:4]))
        elif '|' in regex:
            return BarTree(build_regex_tree(regex[1:2]),
                           build_regex_tree(regex[3:4]))
    elif regex[0] is '(' and regex[-1] is ')' and ('.' in regex or '|' in
                                                   regex):
        if regex[1:-1][0] is '(':
            index = index_of_left_closing_bracket(regex[1:-1])
            index_of_separator = index + 1
            if regex[1:-1][index_of_separator] is '*':
                index_of_separator += 1
            separator = regex[1:-1][index_of_separator]
        elif regex[1:-1][-1] is ')':
            index_of_first_open_bracket = regex[1:-1].find('(')
            index_of_separator = index_of_first_open_bracket - 1
            separator = regex[1:-1][index_of_separator]
        else:
            if '(' in regex[1:-1]:
                index_of_first_open_bracket = regex[1:-1].find('(')
                index_of_separator = index_of_first_open_bracket - 1
                separator = regex[1:-1][index_of_separator]
            else:
                index_of_separator = max(regex[1:-1].find('.'), regex[1:-1].find('|'))
                separator = regex[1:-1][index_of_separator]
        
        if separator is '.':
            return DotTree(build_regex_tree(regex[1:-1][:index_of_separator]),
                           build_regex_tree(regex[1:-1][index_of_separator + 1:]))
        elif separator is '|':
            return BarTree(build_regex_tree(regex[1:-1][:index_of_separator]),
                           build_regex_tree(regex[1:-1][index_of_separator + 1:]))
        
if __name__ == '__main__':
    import doctest
    doctest.testmod()
