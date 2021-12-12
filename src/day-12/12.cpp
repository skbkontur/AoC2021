#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <string>
#include <vector>
#include <algorithm>
#include <fstream>

using std::cout;
using std::unordered_map;
using std::unordered_set;
using std::string;
using std::transform;
using std::vector;
using std::ifstream;

class TraversalPolicy {
public:
    virtual void PushVisit(const string& node) = 0;
    virtual void PopVisit(const string& node) = 0;
    virtual bool CanVisit(const string& node) = 0;
};

class Graph: public unordered_map<string, vector<string>> {
public:
    void Load(ifstream& in) {
        string s;
        while (in >> s) {
            size_t hyphenPos = s.find('-');
            string lhs = s.substr(0, hyphenPos);
            string rhs = s.substr(hyphenPos + 1, string::npos);
            (*this)[lhs].push_back(rhs);
            (*this)[rhs].push_back(lhs);
        }
    }

    size_t NumberOfPaths(string start, string finish, std::shared_ptr<TraversalPolicy> policy) const {
        if (start == finish) {
            return 1;
        }
        auto neighbours = this->find(start);
        if (neighbours == this->end()) {
            return 0;
        }
        size_t result = 0;
        for (auto&& it : neighbours->second) {
            if (!policy->CanVisit(it)) {
                continue;
            }
            policy->PushVisit(it);
            result += this->NumberOfPaths(it, finish, policy);
            policy->PopVisit(it);
        }
        return result;
    }

    template <typename Policy>
    size_t NumberOfPaths() const {
        return NumberOfPaths("start", "end", std::make_shared<Policy>("start"));
    }
};

string Upper(const string& str) {
    string upper = str;
    transform(upper.begin(), upper.end(),upper.begin(), ::toupper);
    return upper;
}

string Lower(const string& str) {
    string lower = str;
    transform(lower.begin(), lower.end(),lower.begin(), ::tolower);
    return lower;
}

bool IsUpper(const string& str) {
    return Upper(str) == str;
}

bool IsLower(const string& str) {
    return Lower(str) == str;
}

class FirstPuzzlePolicy: public TraversalPolicy {
private:
    unordered_map<string, int> Counter;
public:
    FirstPuzzlePolicy(string start) {
        ++Counter[start];
    }

    ~FirstPuzzlePolicy() {
    }

    void PushVisit(const string& node) override {
        ++Counter[node];
    }
    void PopVisit(const string& node) override {
        --Counter[node];
    }
    bool CanVisit(const string& node) override {
        return IsUpper(node) || Counter[node] == 0;
    }
};

class SecondPuzzlePolicy: public TraversalPolicy {
private:
    unordered_map<string, int> Counter;
    int NumTwiceVisited;
public:
    SecondPuzzlePolicy(string start)
        : NumTwiceVisited(0)
    {
        ++Counter[start];
    }

    ~SecondPuzzlePolicy() {
    }

    void PopVisit(const string& node) override {
        if (Counter[node]-- == 2 && IsLower(node)) {
            --NumTwiceVisited;
        }
    }

    void PushVisit(const string& node) override {
        if (++Counter[node] == 2 && IsLower(node)) {
            ++NumTwiceVisited;
        }
    }

    bool CanVisit(const string& node) override {
        if (node == "start") {
            return false;
        }
        if (IsUpper(node)) {
            return true;
        }
        if (IsLower(node) && NumTwiceVisited == 0) {
            return Counter[node] < 2;
        }
        return Counter[node] == 0;
    }
};

int main() {
    std::ifstream in("/Users/ashagraev/Desktop/input.txt");
    Graph g;
    g.Load(in);
    cout << "default policy paths: " << g.NumberOfPaths<FirstPuzzlePolicy>() << std::endl;
    cout << "second policy paths: " << g.NumberOfPaths<SecondPuzzlePolicy>() << std::endl;
    return 0;
}
