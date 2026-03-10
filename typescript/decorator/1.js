"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
// 클래스나 메서드를 감싸서 기능을 확장
class UserService {
    getUser(name) {
        return `User: ${name}`;
    }
}
__decorate([
    Log,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", String)
], UserService.prototype, "getUser", null);
const service = new UserService();
service.getUser("Alex");
// TypeScript에서 메서드 데코레이터의 시그니처
// function decorator(
//   target: any,
//   propertyKey: string,
//   descriptor: PropertyDescriptor
// ) 
// 로그 데코레이터
function Log(target, // 어디에 붙어있는지
propertyKey, // 메서드 이름 (getUser)
descriptor // { value: Function, writable: boolean, enumerable: boolean, configurable: boolean }
) {
    const originalMethod = descriptor.value; // 백업
    // 함수 교체
    descriptor.value = function (...args) {
        console.log(`[LOG] ${propertyKey} called with`, args);
        return originalMethod.apply(this, args); // this 유지하면서 함수 실행
    };
    return descriptor;
}
