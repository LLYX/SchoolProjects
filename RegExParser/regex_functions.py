"""
# Copyright 2013 Nick Cheng, Brian Harrington, Danny Heap, Leon Xu 2013, 2014
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

def is_regex(s):

    if s.count('(') is not s.count(')'):
        return False
    elif s is '':
        return False
    elif s in ('0', '1', '2', 'e'):
        return True
    elif s[-1] is '*':
        return is_regex(s[:-1])
    elif (s[0] is '(' and s[-1] is ')' and
          len(s) is 5 and ('.' in s or '|' in s)):
        return is_regex(s[1:2]) and is_regex(s[3:4])
    elif s[0] is '(' and s[-1] is ')' and ('.' in s or '|' in s):
        if s[1:-1][0] is '(':
            index = index_of_left_closing_bracket(s[1:-1])
            index_of_separator = index + 1
            if s[1:-1][index_of_separator] is '*':
                index_of_separator += 1
        elif s[1:-1][-1] is ')':
            index_of_first_open_bracket = s[1:-1].find('(')
            index_of_separator = index_of_first_open_bracket - 1
        else:
            if s[1:-1][-1] is '*':
                return is_regex('(' + s[1:-2] + ')')
            else:
                index_of_separator = max(s[1:-1].find('.'), s[1:-1].find('|'))
        
        return (is_regex(s[1:-1][:index_of_separator]) and
                is_regex(s[1:-1][index_of_separator + 1:]))
    
    return False

def index_of_left_closing_bracket(s):
    number_of_open_brackets = 0
    number_of_closed_brackets = 0
    index = 0
    for c in s:
        if c is '(':
            number_of_open_brackets += 1
        elif c is ')':
            number_of_closed_brackets += 1

        if number_of_open_brackets is number_of_closed_brackets:
            return index
        else:
            index += 1

def all_regex_permutations(s):

    combinations = permutate(s)

    select_regex(combinations)
    
    return set(combinations)

def permutate(s):

    for c in s:
        if c not in ('0', '1', '2', 'e', '*', '.', '|', '(', ')'):
            s = s.replace(c, '')

    if len(s) < 2:
        return [s]
    
    rest = permutate(s[1:])
    first_char = s[0]
    permutations = []
                       
    for string in rest:
        for i in range(len(string) + 1):
            permutations.append(string[:i] + first_char + string[i:])
    
    return permutations

def select_regex(l):

    l[:] = (e for e in l if is_regex(e))
            
def regex_match(r, s):

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
    
def build_regex_tree(regex):

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
